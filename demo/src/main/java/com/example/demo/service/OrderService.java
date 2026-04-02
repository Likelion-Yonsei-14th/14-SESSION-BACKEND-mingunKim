package com.example.demo.service;

import com.example.demo.discount.RateDiscountPolicy;
import com.example.demo.discount.DiscountPolicy;
import com.example.demo.member.Member;

public class OrderService {
    private DiscountPolicy discountPolicy = new RateDiscountPolicy();

    public int createOrder(Member member, int price) {
        return price - discountPolicy.discount(member, price);
    }
}