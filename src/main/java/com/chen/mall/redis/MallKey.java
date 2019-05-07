package com.chen.mall.redis;

public class MallKey extends BasePrefix{

	private MallKey(String prefix) {
		super(prefix);
	}
	public static MallKey isGoodsOver = new MallKey("go");
}
