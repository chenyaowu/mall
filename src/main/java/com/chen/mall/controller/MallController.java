package com.chen.mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.chen.mall.domain.MallOrder;
import com.chen.mall.domain.MallUser;
import com.chen.mall.domain.OrderInfo;
import com.chen.mall.redis.RedisService;
import com.chen.mall.result.CodeMsg;
import com.chen.mall.service.GoodsService;
import com.chen.mall.service.MallService;
import com.chen.mall.service.MallUserService;
import com.chen.mall.service.OrderService;
import com.chen.mall.vo.GoodsVo;

@Controller
@RequestMapping("/mall")
public class MallController {

	@Autowired
	MallUserService userService;

	@Autowired
	RedisService redisService;

	@Autowired
	GoodsService goodsService;

	@Autowired
	OrderService orderService;

	@Autowired
	MallService mallService;

    @RequestMapping("/do_mall")
    public String list(Model model,MallUser user,
    		@RequestParam("goodsId")long goodsId) {
    	model.addAttribute("user", user);
    	if(user == null) {
    		return "login";
    	}
    	//判断库存
    	GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
    	int stock = goods.getStockCount();
    	if(stock <= 0) {
    		model.addAttribute("errmsg", CodeMsg.MALL_OVER.getMsg());
    		return "mall_fail";
    	}
    	//判断是否已经秒杀到了
    	MallOrder order = orderService.getMallOrderByUserIdGoodsId(user.getId(), goodsId);
    	if(order != null) {
    		model.addAttribute("errmsg", CodeMsg.REPEATE_MALL.getMsg());
    		return "mall_fail";
    	}
    	//减库存 下订单 写入秒杀订单
    	OrderInfo orderInfo = mallService.mall(user, goods);
    	model.addAttribute("orderInfo", orderInfo);
    	model.addAttribute("goods", goods);
        return "order_detail";
    }
}
