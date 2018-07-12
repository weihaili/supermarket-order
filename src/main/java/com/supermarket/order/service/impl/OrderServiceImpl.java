package com.supermarket.order.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.supermarket.common.utils.KklResult;
import com.supermarket.mapper.TbOrderItemMapper;
import com.supermarket.mapper.TbOrderMapper;
import com.supermarket.mapper.TbOrderShippingMapper;
import com.supermarket.order.dao.JedisClient;
import com.supermarket.order.pojo.OderState;
import com.supermarket.order.pojo.Order;
import com.supermarket.order.service.OrderService;
import com.supermarket.order.util.OrderStatus;
import com.supermarket.pojo.TbOrder;
import com.supermarket.pojo.TbOrderExample;
import com.supermarket.pojo.TbOrderItem;
import com.supermarket.pojo.TbOrderItemExample;
import com.supermarket.pojo.TbOrderItemExample.Criteria;
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
		if (StringUtils.isBlank(orderId)) {
			return KklResult.build(400, "The parameter orderId is null,please check");
		}
		
		Order order = new Order();
		TbOrder tbOrder = orderMapper.selectByPrimaryKey(orderId);
		
		TbOrderItemExample example = new TbOrderItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andOrderIdEqualTo(orderId);
		List<TbOrderItem> orderItems = orderItemMapper.selectByExample(example);
		
		TbOrderShipping orderShipping = orderShippingMapper.selectByPrimaryKey(orderId);
		
		order.setOrderId(orderId);
		order.setPayment(tbOrder.getPayment());
		order.setPaymentType(tbOrder.getPaymentType());
		order.setStatus(tbOrder.getStatus());
		order.setCreateTime(tbOrder.getCreateTime());
		order.setPostFee(tbOrder.getPostFee());
		order.setUserId(tbOrder.getUserId());
		order.setBuyerMessage(tbOrder.getBuyerMessage());
		order.setBuyerNick(tbOrder.getBuyerNick());
		
		order.setOrderItems(orderItems);
		order.setOrderShipping(orderShipping);
		
		return KklResult.ok(order);
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
		if (null==userId) {
			return KklResult.build(400, "The parameter userId is null,please check");
		}
		TbOrderExample example=new TbOrderExample();
		com.supermarket.pojo.TbOrderExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		PageHelper.startPage(page, count);
		List<TbOrder> list = orderMapper.selectByExample(example);
		List<Order> orders=new ArrayList<Order>();
		for (TbOrder tbOrder : list) {
			Order order = new Order();
			order.setOrderId(tbOrder.getOrderId());
			order.setPayment(tbOrder.getPayment());
			order.setPaymentType(tbOrder.getPaymentType());
			order.setStatus(tbOrder.getStatus());
			order.setCreateTime(tbOrder.getCreateTime());
			order.setPostFee(tbOrder.getPostFee());
			order.setUserId(tbOrder.getUserId());
			order.setBuyerMessage(tbOrder.getBuyerMessage());
			order.setBuyerNick(tbOrder.getBuyerNick());
			orders.add(order);
		}
		return KklResult.ok(orders);
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
		if (state==null) {
			return KklResult.build(400, "The modify order status parameter is null,please check");
		}
		
		TbOrder order = orderMapper.selectByPrimaryKey(state.getOrderId());
		if (order==null) {
			return KklResult.build(400, "This orderId "+state.getOrderId()+"is not exist,please check");
		}
		
		order.setStatus(state.getStatus());
		order.setPaymentTime(new Date(Long.parseLong(state.getPaymentTime())));
		order.setUpdateTime(new Date(Long.parseLong(state.getPaymentTime())));
		
		return KklResult.ok();
	}
	
	

}
