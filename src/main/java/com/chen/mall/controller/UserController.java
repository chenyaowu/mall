package com.chen.mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chen.mall.domain.MallUser;
import com.chen.mall.redis.RedisService;
import com.chen.mall.result.Result;
import com.chen.mall.service.MallUserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
    MallUserService userService;
	
	@Autowired
	RedisService redisService;
	
    @RequestMapping("/info")
    @ResponseBody
    public Result<MallUser> info(Model model,MallUser user) {
        return Result.success(user);
    }
    
}
