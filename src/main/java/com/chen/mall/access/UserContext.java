package com.chen.mall.access;


import com.chen.mall.domain.MallUser;

public class UserContext {
	
	private static ThreadLocal<MallUser> userHolder = new ThreadLocal<MallUser>();
	
	public static void setUser(MallUser user) {
		userHolder.set(user);
	}
	
	public static MallUser getUser() {
		return userHolder.get();
	}

}
