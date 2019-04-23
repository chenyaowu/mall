package com.chen.mall.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chen.mall.dao.MallUserDao;
import com.chen.mall.domain.MallUser;
import com.chen.mall.exception.GlobalException;
import com.chen.mall.redis.MallUserKey;
import com.chen.mall.redis.RedisService;
import com.chen.mall.result.CodeMsg;
import com.chen.mall.util.MD5Util;
import com.chen.mall.util.UUIDUtil;
import com.chen.mall.vo.LoginVo;

@Service
public class MallUserService {
	
	
	public static final String COOKI_NAME_TOKEN = "token";
	
	@Autowired
	MallUserDao mallUserDao;
	
	@Autowired
	RedisService redisService;
	
	public MallUser getById(long id) {
		return mallUserDao.getById(id);
	}
	

	public MallUser getByToken(HttpServletResponse response, String token) {
		if(StringUtils.isEmpty(token)) {
			return null;
		}
		MallUser user = redisService.get(MallUserKey.token, token, MallUser.class);
		//延长有效期
		if(user != null) {
			addCookie(response, token, user);
		}
		return user;
	}
	

	public boolean login(HttpServletResponse response, LoginVo loginVo) {
		if(loginVo == null) {
			throw new GlobalException(CodeMsg.SERVER_ERROR);
		}
		String mobile = loginVo.getMobile();
		String formPass = loginVo.getPassword();
		//判断手机号是否存在
		MallUser user = getById(Long.parseLong(mobile));
		if(user == null) {
			throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
		}
		//验证密码
		String dbPass = user.getPassword();
		String saltDB = user.getSalt();
		String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
		if(!calcPass.equals(dbPass)) {
			throw new GlobalException(CodeMsg.PASSWORD_ERROR);
		}
		//生成cookie
		String token = UUIDUtil.uuid();
		addCookie(response, token, user);
		return true;
	}
	
	private void addCookie(HttpServletResponse response, String token, MallUser user) {
		redisService.set(MallUserKey.token, token, user);
		Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
		cookie.setMaxAge(MallUserKey.token.expireSeconds());
		cookie.setPath("/");
		response.addCookie(cookie);
	}

}
