package com.supermarket.order.pojo;

import java.io.Serializable;
import java.util.List;

import com.supermarket.pojo.TbOrder;
import com.supermarket.pojo.TbOrderItem;
import com.supermarket.pojo.TbOrderShipping;

public class Order extends TbOrder implements Serializable{
	
	/**
	 * @fieldName: serialVersionUID   
	 * @Description this pojo maybe network transmit
	 * @fieldType: long   
	 */ 
	private static final long serialVersionUID = 7306531457044963866L;

	private List<TbOrderItem> orderItems;
	
	private TbOrderShipping orderShipping;

	public List<TbOrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<TbOrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public TbOrderShipping getOrderShipping() {
		return orderShipping;
	}

	public void setOrderShipping(TbOrderShipping orderShipping) {
		this.orderShipping = orderShipping;
	}

}
