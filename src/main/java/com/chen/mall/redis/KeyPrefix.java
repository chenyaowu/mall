package com.chen.mall.redis;

public interface KeyPrefix {
		
	int expireSeconds();
	
	String getPrefix();
	
}
