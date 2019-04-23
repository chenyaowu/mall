package com.chen.mall.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.chen.mall.domain.MallUser;

@Mapper
public interface MallUserDao {
	
	@Select("select * from mall_user where id = #{id}")
	MallUser getById(@Param("id") long id);
}
