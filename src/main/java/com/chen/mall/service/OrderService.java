package com.chen.mall.service;

import java.util.Date;

import com.chen.mall.redis.OrderKey;
import com.chen.mall.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chen.mall.dao.OrderDao;
import com.chen.mall.domain.MallOrder;
import com.chen.mall.domain.MallUser;
import com.chen.mall.domain.OrderInfo;
import com.chen.mall.vo.GoodsVo;

@Service
public class OrderService {
	
	@Autowired
	OrderDao orderDao;
	@Autowired
	RedisService redisService;

	public MallOrder getMallOrderByUserIdGoodsId(long userId, long goodsId) {
		return redisService.get(OrderKey.getMallOrderByUidGid, ""+userId+"_"+goodsId, MallOrder.class);
	}
	@Transactional
	public OrderInfo createOrder(MallUser user, GoodsVo goods) {
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setCreateDate(new Date());
		orderInfo.setDeliveryAddrId(0L);
		orderInfo.setGoodsCount(1);
		orderInfo.setGoodsId(goods.getId());
		orderInfo.setGoodsName(goods.getGoodsName());
		orderInfo.setGoodsPrice(goods.getMallPrice());
		orderInfo.setOrderChannel(1);
		orderInfo.setStatus(0);
		orderInfo.setUserId(user.getId());
		long orderId = orderDao.insert(orderInfo);
		MallOrder mallOrder = new MallOrder();
		mallOrder.setGoodsId(goods.getId());
		mallOrder.setOrderId(orderId);
		mallOrder.setUserId(user.getId());
		orderDao.insertMallOrder(mallOrder);
		return orderInfo;
	}

	public OrderInfo getOrderById(long orderId) {
		return orderDao.getOrderById(orderId);
	}
	
}
