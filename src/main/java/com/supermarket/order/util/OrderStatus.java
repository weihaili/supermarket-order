package com.supermarket.order.util;

/**   
 * @ClassName: OrderStatus   
 * @Description: 1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭',  
 * @author: KKL 
 * @date: 2018年7月11日 下午4:00:20      
 */  
public enum OrderStatus {
	
	ONE(1,"未付款"),TWO(2,"已付款"),THREE(3,"未发货"),FOUR(4,"已发货"),FIVE(5,"交易成功"),SIX(6,"交易关闭");
	
	public Integer getNum() {
		return num;
	}

	public String getStateDesc() {
		return stateDesc;
	}

	private OrderStatus(Integer num, String stateDesc) {
		this.num = num;
		this.stateDesc = stateDesc;
	}

	private Integer num;
	
	private String stateDesc;
}
