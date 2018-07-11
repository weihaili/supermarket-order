package com.supermarket.order.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.supermarket.common.utils.KklResult;
import com.supermarket.mapper.TbOrderItemMapper;
import com.supermarket.mapper.TbOrderMapper;
import com.supermarket.mapper.TbOrderShippingMapper;
import com.supermarket.order.dao.JedisClient;
import com.supermarket.order.pojo.OderState;
import com.supermarket.order.service.OrderService;
import com.supermarket.order.util.OrderStatus;
import com.supermarket.pojo.TbOrder;
import com.supermarket.pojo.TbOrderItem;
import com.supermarket.pojo.TbOrderShipping;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private TbOrderMapper orderMapper;
	
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	
	@Autowired
	private TbOrderShippingMapper orderShippingMapper;
	
	@Autowired
	@Qualifier("jedisClinetSingle")
	private JedisClient jedisClient;
	
	@Value("${ORDER_GENERATE_KEY}")
	private String ORDER_GEN_KEY;
	
	@Value("${ORDER_INIT_ID}")
	private String ORDER_INIT_ID;
	
	@Value("${ORDER_DETAILS_GENERATE_KEY}")
	private String ORDER_DETAILS_GEN_KEY;

	/**   
	 * <p>Title: createOrder</p>   
	 * <p>Description: save new order
	 * 1. revoke redis service get an order id
	 * 2. supplement pojo content
	 * 3. save pojo to mysql
	 * </p>   
	 * @param order
	 * @param orderItems
	 * @param orderShipping
	 * @return   
	 * @see com.supermarket.order.service.OrderService#createOrder(com.supermarket.pojo.TbOrder, java.util.List, com.supermarket.pojo.TbOrderShipping)   
	 */ 
	@Override
	public KklResult createOrder(TbOrder order, List<TbOrderItem> orderItems, TbOrderShipping orderShipping) {
		Date date = new Date();
		String string = jedisClient.get(ORDER_GEN_KEY);
		if (StringUtils.isBlank(string)) {
			jedisClient.set(ORDER_GEN_KEY, ORDER_INIT_ID);
		}
		long orderId = jedisClient.incr(ORDER_GEN_KEY);
		String orderIdStr="KKL"+orderId;
		order.setOrderId(orderIdStr);
		order.setCreateTime(date);
		order.setUpdateTime(date);
		order.setStatus(OrderStatus.ONE.getNum());
		//0:not evaluated  1:already evaluated
		order.setBuyerRate(0);
		orderMapper.insertSelective(order);
		
		for (TbOrderItem tbOrderItem : orderItems) {
			long orderDetailId = jedisClient.incr(ORDER_DETAILS_GEN_KEY);
			tbOrderItem.setId("OD"+orderDetailId);
			tbOrderItem.setOrderId(orderIdStr);
			orderItemMapper.insertSelective(tbOrderItem);
		}
		
		orderShipping.setOrderId(orderIdStr);
		orderShipping.setCreated(date);
		orderShipping.setUpdated(date);
		orderShippingMapper.insertSelective(orderShipping);
		
		return KklResult.ok(orderIdStr);
	}

	/**   
	 * <p>Title: getOrderInfoByOrderId</p>   
	 * <p>Description: get order information by order id</p>   
	 * @param orderId
	 * @return   
	 * @see com.supermarket.order.service.OrderService#getOrderInfoByOrderId(java.lang.String)   
	 */ 
	@Override
	public KklResult getOrderInfoByOrderId(String orderId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**   
	 * <p>Title: getOderListByUserId</p>   
	 * <p>Description: get order list by user id</p>   
	 * @param userId
	 * @param page
	 * @param count
	 * @return   
	 * @see com.supermarket.order.service.OrderService#getOderListByUserId(java.lang.Long, java.lang.Integer, java.lang.Integer)   
	 */ 
	@Override
	public KklResult getOderListByUserId(Long userId, Integer page, Integer count) {
		// TODO Auto-generated method stub
		return null;
	}

	/**   
	 * <p>Title: updateOrderStatus</p>   
	 * <p>Description: update order status by order id</p>   
	 * @param state
	 * @return   
	 * @see com.supermarket.order.service.OrderService#updateOrderStatus(com.supermarket.order.pojo.OderState)   
	 */ 
	@Override
	public KklResult updateOrderStatus(OderState state) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
