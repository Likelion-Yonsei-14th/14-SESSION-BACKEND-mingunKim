package com.example.demo.service;

import com.example.demo.discount.DiscountPolicy;
import com.example.demo.member.Member;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final DiscountPolicy discountPolicy;

    public OrderService(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    public int createOrder(Member member, int price) {
        return price - discountPolicy.discount(member, price);
    }
}
