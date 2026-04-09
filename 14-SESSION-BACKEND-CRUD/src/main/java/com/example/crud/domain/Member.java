package com.example.crud.domain;

import jakarta.persistence.*;

@Entity // 클래스가 DB 테이블과 1:1로 매칭되도록 함
public class Member {

    @Id // 이 필드가 Primary Key(PK)임을 뜻함
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID를 1, 2, 3... 자동으로 생성
    private Long id;

    private String name;
    private String email;

    public Member() {
    }

    // Getter & Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
