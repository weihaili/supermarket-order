package com.supermarket.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermarket.common.utils.ExceptionUtil;
import com.supermarket.common.utils.KklResult;
import com.supermarket.order.pojo.OderState;
import com.supermarket.order.pojo.Order;
import com.supermarket.order.service.OrderService;

@Controller
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@RequestMapping(value="/create",method=RequestMethod.POST)
	@ResponseBody
	public KklResult createNewOrder(@RequestBody Order order) {
		try {
			KklResult result = orderService.createOrder(order, order.getOrderItems(), order.getOrderShipping());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return KklResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
	
	/**   
	 * @Title: getOrderInfoByOrderId   
	 * @Description: get order information by order id   
	 * @param: @param orderId
	 * @param: @return      
	 * @return: KklResult      
	 * @throws   
	 */ 
	@RequestMapping("/info/{orderId}")
	@ResponseBody
	public KklResult getOrderInfoByOrderId(@PathVariable String orderId) {
		KklResult result= orderService.getOrderInfoByOrderId(orderId);
		return result;
	}
	
	@RequestMapping("/{userID}/{page}/{count}")
	@ResponseBody
	public KklResult getOderListByUserId(@PathVariable Long userId,@PathVariable Integer page,@PathVariable Integer count ) {
		KklResult result= orderService.getOderListByUserId(userId,page,count);
		return result;
	}
	
	/**   
	 * @Title: updateOrderStateByOrderId   
	 * @Description: update order status by order id   
	 * @param: @param state
	 * @param: @return      
	 * @return: KklResult      
	 * @throws   
	 */ 
	@RequestMapping(value="/changeStatus",method=RequestMethod.POST)
	@ResponseBody
	public KklResult updateOrderStateByOrderId(OderState state) {
		KklResult result = orderService.updateOrderStatus(state);
		return result;
	}

}
