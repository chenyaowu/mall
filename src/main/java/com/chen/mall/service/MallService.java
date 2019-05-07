package com.chen.mall.service;

import com.chen.mall.domain.MallOrder;
import com.chen.mall.redis.MallKey;
import com.chen.mall.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chen.mall.domain.MallUser;
import com.chen.mall.domain.OrderInfo;
import com.chen.mall.vo.GoodsVo;

import java.util.List;

@Service
public class MallService {
	
	@Autowired
	GoodsService goodsService;
	
	@Autowired
	OrderService orderService;
	@Autowired
	RedisService redisService;

	@Transactional
	public OrderInfo mall(MallUser user, GoodsVo goods) {
		//减库存 下订单 写入秒杀订单
		boolean success = goodsService.reduceStock(goods);
		if(success) {
			//order_info maiosha_order
			return orderService.createOrder(user, goods);
		}else {
			setGoodsOver(goods.getId());
			return null;
		}
	}
	public long getMallResult(Long userId, long goodsId) {
		MallOrder order = orderService.getMallOrderByUserIdGoodsId(userId, goodsId);
		if(order != null) {//秒杀成功
			return order.getOrderId();
		}else {
			boolean isOver = getGoodsOver(goodsId);
			if(isOver) {
				return -1;
			}else {
				return 0;
			}
		}
	}


	private void setGoodsOver(Long goodsId) {
		redisService.set(MallKey.isGoodsOver, ""+goodsId, true);
	}

	private boolean getGoodsOver(long goodsId) {
		return redisService.exists(MallKey.isGoodsOver, ""+goodsId);
	}
	public void reset(List<GoodsVo> goodsList) {
		goodsService.resetStock(goodsList);
		orderService.deleteOrders();
	}
	
}
