package com.example.demo.member;

public class Member {
    private String name;
    private String grade; // "VIP", "BASIC"

    public Member(String name, String grade) {
        this.name = name;
        this.grade = grade;
    }

    public String getGrade() {
        return grade;
    }
}