package com.example.demo.discount;

import com.example.demo.member.Member;
import org.springframework.stereotype.Component;

@Component
public class FixDiscountPolicy implements DiscountPolicy {

    @Override

    public int discount(Member member, int price) {

        if (member.getGrade().equals("VIP")) {

            return 1000;

        }

        return 0;

    }

}