package com.example.demo;

import com.example.demo.member.Member;
import com.example.demo.service.OrderService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.example.demo.config.AppConfig;

public class DemoApplication {
	public static void main(String[] args) {
		ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
		OrderService orderService = ac.getBean(OrderService.class);
		Member member = new Member("홍길동", "VIP");
		int result = orderService.createOrder(member, 20000);
		System.out.println("결제 금액: " + result);
	}
}