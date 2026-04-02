package com.example.demo;

import com.example.demo.member.Member;
import com.example.demo.service.OrderService;

public class DemoApplication {
	public static void main(String[] args) {
		OrderService orderService = new OrderService();
		Member member = new Member("홍길동", "VIP");
		int result = orderService.createOrder(member, 20000);
		System.out.println("결제 금액: " + result);
	}
}