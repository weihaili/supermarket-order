package com.supermarket.order.service;

import java.util.List;

import com.supermarket.common.utils.KklResult;
import com.supermarket.pojo.TbOrder;
import com.supermarket.pojo.TbOrderItem;
import com.supermarket.pojo.TbOrderShipping;

public interface OrderService {
	
	/**   
	 * @Title: createOrder   
	 * @Description: create new order,new oder list,new order shopping   
	 * @param: @param order
	 * @param: @param orderItems
	 * @param: @param orderShipping
	 * @param: @return      
	 * @return: KklResult      
	 * @throws   
	 */ 
	KklResult createOrder(TbOrder order,List<TbOrderItem> orderItems,TbOrderShipping orderShipping);

}
