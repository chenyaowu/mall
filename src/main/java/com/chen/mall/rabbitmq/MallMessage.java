package com.chen.mall.rabbitmq;

import com.chen.mall.domain.MallUser;

public class MallMessage {
	private MallUser user;
	private long goodsId;
	public MallUser getUser() {
		return user;
	}
	public void setUser(MallUser user) {
		this.user = user;
	}
	public long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(long goodsId) {
		this.goodsId = goodsId;
	}
}
