package com.chen.mall.controller;

import com.chen.mall.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/mall")
public class MallController {
	private static Logger log = LoggerFactory.getLogger(MallController.class);
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

	@RequestMapping(value="/do_mall", method= RequestMethod.POST)
	@ResponseBody
	public Result<OrderInfo> miaosha(Model model, MallUser user,
									 @RequestParam("goodsId")long goodsId) {
		model.addAttribute("user", user);
		if(user == null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		//判断库存
		GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
		int stock = goods.getStockCount();
		if(stock <= 0) {
			return Result.error(CodeMsg.MALL_OVER);
		}
		//判断是否已经秒杀到了
		MallOrder order = orderService.getMallOrderByUserIdGoodsId(user.getId(), goodsId);
		if(order != null) {
			return Result.error(CodeMsg.REPEATE_MALL);
		}
		//减库存 下订单 写入秒杀订单
		OrderInfo orderInfo = mallService.mall(user, goods);
		return Result.success(orderInfo);
	}
}
