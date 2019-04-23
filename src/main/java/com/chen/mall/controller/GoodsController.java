package com.chen.mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chen.mall.domain.MallUser;
import com.chen.mall.redis.RedisService;
import com.chen.mall.service.MallUserService;

@Controller
@RequestMapping("/goods")
public class GoodsController {

	@Autowired
	MallUserService userService;
	
	@Autowired
	RedisService redisService;
	
    @RequestMapping("/to_list")
    public String list(Model model,MallUser user) {
    	model.addAttribute("user", user);
        return "goods_list";
    }
    
}
