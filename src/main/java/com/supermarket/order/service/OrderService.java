package com.supermarket.order.service;

import java.util.List;

import com.supermarket.common.utils.KklResult;
import com.supermarket.order.pojo.OderState;
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

	/**   
	 * @Title: getOrderInfoByOrderId   
	 * @Description: get order information by order id
	 * @param: @param orderId
	 * @param: @return      
	 * @return: KklResult      
	 * @throws   
	 */ 
	KklResult getOrderInfoByOrderId(String orderId);

	/**   
	 * @Title: getOderListByUserId   
	 * @Description: get order list by user id
	 * @param: @param userId
	 * @param: @param page
	 * @param: @param count
	 * @param: @return      
	 * @return: KklResult      
	 * @throws   
	 */ 
	KklResult getOderListByUserId(Long userId, Integer page, Integer count);

	/**   
	 * @Title: updateOrderStatus   
	 * @Description: update order status by order id   
	 * @param: @param state
	 * @param: @return      
	 * @return: KklResult      
	 * @throws   
	 */ 
	KklResult updateOrderStatus(OderState state);

}
