# 📌 결제 시스템 리팩토링 과제

## 🎯 과제 개요

기존의 결제 시스템 코드를 분석하고,
객체지향적으로 구조를 개선하는 것을 목표로 합니다.

---

## 1️⃣ 기존 코드의 문제점

* **캡슐화 위반**: `Payment`의 필드가 `private`으로 선언되어 있음에도 `PaymentService`에서 `payment.type = type`과 같이 직접 접근하고 있었습니다. 이는 Java에서 컴파일 에러를 유발하며, 객체 내부 상태를 외부에서 마음대로 변경할 수 있는 구조입니다.
* **생성자 미완성**: `Payment(type, amount)` 형태로 파라미터 타입 선언이 없었고, 생성자 내부도 비어 있어 필드 초기화가 전혀 이루어지지 않았습니다.
* **잘못된 언어 문법 혼용**: `def`, `self.` 등 Python 문법이 Java 파일에 그대로 사용되어 컴파일 자체가 불가능한 상태였습니다.
* **책임이 분산되어 있지 않음**: 금액 검증(`amount <= 0`)과 결제 타입 분기(`type.equals("card")`) 로직이 Payment 객체 안에 있었지만, `PaymentService`가 객체 생성과 필드 설정까지 직접 담당하고 있어 책임 경계가 모호했습니다.
* **Service의 역할 과다**: `PaymentService`가 결제 객체 생성, 필드 설정, 검증 메서드 호출, 타입 처리 메서드 호출까지 모두 담당하고 있었습니다.

---

## 2️⃣ 로직 이동 내용

### 🔹 변경 전

* `PaymentService`에서 `payment.type = type`, `payment.amount = amount`로 필드를 직접 설정
* `PaymentService`에서 `payment.do_pay()`와 `payment.check_type()`을 각각 별도로 호출

### 🔹 변경 후

* `Payment` 생성자(`Payment(String type, int amount)`)에서 필드 초기화를 담당
* 금액 검증(`amount <= 0`)은 `Payment.pay()` 내부에서 처리
* 결제 타입 분기는 `Payment.processType()`(private)으로 분리하여 `pay()` 내부에서만 호출
* `PaymentService`는 `new Payment(type, amount).pay()` 형태로 단순화

👉 **이렇게 바꾼 이유**: 금액과 타입은 Payment 객체 자신의 데이터입니다. 자신의 데이터에 대한 검증과 처리는 객체 스스로 책임지는 것이 객체지향의 기본 원칙(Tell, Don't Ask)에 부합합니다. Service가 객체 내부를 알아야 하는 구조는 결합도를 높이고, Payment의 필드 구조가 바뀌면 Service도 함께 수정해야 하는 문제가 생깁니다.

---

## 3️⃣ 구조 개선 내용

* **생성자를 통한 완전한 초기화**: 객체가 생성되는 시점에 반드시 `type`과 `amount`가 설정되므로, 불완전한 상태의 Payment 객체가 존재할 수 없습니다.
* **객체가 직접 일을 함**: 결제 수행(`pay()`)과 타입 처리(`processType()`)를 Payment 스스로 담당합니다. 외부에서 내부 데이터를 꺼내서 판단하는 대신, 객체에게 행동을 요청하는 구조로 변경되었습니다.
* **Service 단순화**: `PaymentService`는 Payment 객체를 생성하고 `pay()`를 호출하는 역할만 합니다. Payment의 내부 구현이 바뀌어도 Service는 수정할 필요가 없습니다.
* **`processType()` private 처리**: 타입 분기는 `pay()` 흐름 내부에서만 의미가 있으므로 외부에 노출하지 않았습니다. 공개 인터페이스는 `pay()` 하나로 충분합니다.
