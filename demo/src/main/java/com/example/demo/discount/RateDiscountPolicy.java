package com.example.demo.discount;

import com.example.demo.member.Member;

public class RateDiscountPolicy implements DiscountPolicy {

    @Override

    public int discount(Member member, int price) {

        if (member.getGrade().equals("VIP")) {

            return (int) (price * 0.1);

        }

        return 0;

    }

}