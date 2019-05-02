package com.chen.mall.vo;

import java.util.Date;

import com.chen.mall.domain.Goods;

public class GoodsVo extends Goods{
	private Double mallPrice;
	private Integer stockCount;
	private Date startDate;
	private Date endDate;
	public Integer getStockCount() {
		return stockCount;
	}
	public void setStockCount(Integer stockCount) {
		this.stockCount = stockCount;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Double getMallPrice() {
		return mallPrice;
	}
	public void setMallPrice(Double mallPrice) {
		this.mallPrice = mallPrice;
	}
}
