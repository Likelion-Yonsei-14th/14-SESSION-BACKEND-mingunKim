# 📌 3주차 객체지향 & DI 과제

---

## 1️⃣ 문제 상황 분석

### 현재 구조

```java
public class NotificationService {
    private EmailSender emailSender = new EmailSender(); // 직접 생성

    public void notify(String message) {
        emailSender.send(message);
    }
}
```

### 문제점

`NotificationService`가 `EmailSender`라는 **구현체에 직접 의존**하고 있습니다.

즉, 서비스가 "어떤 방식으로 알림을 보낼지"까지 스스로 알고 결정하는 구조입니다.

이는 **단일 책임 원칙(SRP)** 에도 위반됩니다.  
`NotificationService`는 알림을 "보내는 것"에만 집중해야 하는데,  
"어떤 수단으로 보낼지"까지 책임지고 있기 때문입니다.

### SMS, 푸시 알림 추가 시 문제

```java
public class NotificationService {
    private EmailSender emailSender = new EmailSender();
    private SmsSender smsSender = new SmsSender();       // 추가
    private PushSender pushSender = new PushSender();    // 추가

    public void notify(String message, String type) {
        if (type.equals("email")) emailSender.send(message);
        else if (type.equals("sms")) smsSender.send(message);
        else if (type.equals("push")) pushSender.send(message);
    }
}
```

새로운 알림 수단이 생길 때마다 `NotificationService` 코드를 **직접 수정**해야 합니다.

이처럼 **기존 코드를 수정하지 않고 확장할 수 없는 구조**는 OCP(개방-폐쇄 원칙)를 위반합니다.  
변경이 한 곳에서 발생했는데 다른 곳도 연쇄적으로 영향을 받는 **변경 취약 설계**입니다.

---

## 2️⃣ 인터페이스 도입 이유

### 인터페이스 도입 시 장점

```java
public interface NotificationSender {
    void send(String message);
}

public class EmailSender implements NotificationSender { ... }
public class SmsSender implements NotificationSender { ... }
```

```java
public class NotificationService {
    private NotificationSender sender; // 인터페이스에 의존
    ...
}
```

- `NotificationService`는 `NotificationSender`라는 **역할(인터페이스)** 만 알면 됩니다.
- 새로운 구현체(`SmsSender`, `PushSender`)를 추가해도 `NotificationService` 코드는 바뀌지 않습니다.
- 각 구현체는 독립적으로 개발/테스트할 수 있습니다.

### 인터페이스만 도입했을 때 남는 문제

```java
public class NotificationService {
    private NotificationSender sender = new EmailSender(); // 여전히 직접 생성
}
```

인터페이스를 선언해도, **구현체를 직접 `new` 하는 순간 구현체에 의존**하게 됩니다.

> "누가 구현체를 선택하는가?"

이 질문에 아직 `NotificationService` 스스로가 답하고 있습니다.  
진정한 분리가 되려면, **구현체 선택 책임을 외부로 넘겨야** 합니다.

---

## 3️⃣ DIP & DI 이해하기

### DIP (의존성 역전 원칙, Dependency Inversion Principle)

> "고수준 모듈은 저수준 모듈에 의존하면 안 된다. 둘 다 추상화(인터페이스)에 의존해야 한다."

- **고수준 모듈**: `NotificationService` (비즈니스 로직)
- **저수준 모듈**: `EmailSender`, `SmsSender` (세부 구현)

DIP 적용 전에는 `NotificationService → EmailSender` 방향으로 의존했습니다.

DIP 적용 후에는:

```
NotificationService → NotificationSender (인터페이스) ← EmailSender
```

고수준 모듈이 추상화에 의존하고, 저수준 모듈도 추상화를 구현합니다.  
의존 방향이 "역전"됩니다.

### DI (의존성 주입, Dependency Injection)

DI는 DIP를 **실제로 구현하는 방법**입니다.

"의존 관계를 내부에서 직접 생성하지 않고, 외부에서 주입받는다"는 패턴입니다.

```java
// DI 적용: 생성자를 통해 외부에서 주입
public class NotificationService {
    private final NotificationSender sender;

    public NotificationService(NotificationSender sender) {
        this.sender = sender;
    }
}
```

`NotificationService`는 어떤 구현체가 들어오는지 모릅니다.  
외부(AppConfig, Spring 컨테이너 등)가 결정하고 주입합니다.

### 직접 생성 방식 vs DI 방식

| 비교 항목 | 직접 생성 | DI 방식 |
|---|---|---|
| 구현체 결정 주체 | 서비스 내부 | 외부(AppConfig 등) |
| 결합도 | 높음 (구현체에 의존) | 낮음 (인터페이스에 의존) |
| 테스트 용이성 | 어려움 (구현체 교체 불가) | 쉬움 (Mock 주입 가능) |
| 확장성 | 낮음 (코드 수정 필요) | 높음 (새 구현체 추가만) |

---

## 4️⃣ 수동 DI 설계

### 필요한 구성 요소

```
NotificationSender (인터페이스)
├── EmailSender (구현체)
├── SmsSender (구현체)
└── PushSender (구현체)

NotificationService (서비스, 인터페이스에 의존)

AppConfig (설정 클래스, 객체 생성 및 의존성 연결 담당)
```

### 각 구성 요소의 역할

| 구성 요소 | 역할 |
|---|---|
| `NotificationSender` | 알림 전송 기능의 **역할 명세** (인터페이스) |
| `EmailSender` 등 | 각 전송 방식의 **구체적 구현** |
| `NotificationService` | 알림 전송 **비즈니스 로직** 처리, 어떤 구현체인지 모름 |
| `AppConfig` | **객체 생성 + 의존성 연결** 전담 설정 클래스 |

### 객체 생성과 의존성 연결 위치

```java
public class AppConfig {
    public NotificationSender sender() {
        return new EmailSender(); // 어떤 구현체를 쓸지 여기서 결정
    }

    public NotificationService notificationService() {
        return new NotificationService(sender()); // 의존성 주입
    }
}
```

```java
// main
AppConfig config = new AppConfig();
NotificationService service = config.notificationService();
service.notify("회원가입을 축하합니다!");
```

`AppConfig`가 **객체의 생성과 조립을 모두 담당**합니다.  
서비스 클래스들은 자신이 사용할 구현체를 전혀 모르고, 인터페이스만 바라봅니다.

SMS로 바꾸고 싶다면 `AppConfig`의 `sender()` 반환값만 바꾸면 됩니다.  
`NotificationService`는 손댈 필요가 없습니다.

---

## 5️⃣ Spring DI의 필요성

### 수동 DI 방식의 한계

- 객체가 많아질수록 `AppConfig`가 거대해지고 관리가 어렵습니다.
- 객체의 생명주기(생성 시점, 싱글톤 여부 등)를 개발자가 직접 관리해야 합니다.
- 의존 관계가 복잡해지면 `AppConfig` 내 코드가 복잡해집니다.
- 매번 `AppConfig`를 직접 만들고 호출하는 코드를 작성해야 합니다.

### Spring이 해결해주는 것

Spring은 `AppConfig`의 역할을 프레임워크 수준에서 자동화합니다.

- **객체 생성, 등록, 주입**을 스프링 컨테이너가 자동으로 처리합니다.
- 기본적으로 **싱글톤**으로 객체를 관리하여 메모리 낭비를 방지합니다.
- 의존성을 자동으로 탐색하고 연결해줍니다 (`@Autowired`).
- 개발자는 "어떤 객체를 빈으로 등록할지"만 선언하면 됩니다.

### @Configuration, @Bean, @ComponentScan

**`@Configuration`**

> 이 클래스가 스프링 설정 클래스임을 선언합니다.

수동 DI의 `AppConfig`에 해당합니다.  
스프링 컨테이너가 이 클래스를 읽어 빈을 등록합니다.

```java
@Configuration
public class AppConfig {
    @Bean
    public NotificationSender sender() {
        return new EmailSender();
    }
}
```

**`@Bean`**

> 해당 메서드의 반환 객체를 스프링 빈으로 등록합니다.

`@Configuration` 클래스 안에서 사용하며, 반환된 객체가 스프링 컨테이너에 의해 관리됩니다.

**`@ComponentScan`**

> 지정된 패키지를 탐색해서 `@Component` 계열 어노테이션이 붙은 클래스를 자동으로 빈으로 등록합니다.

`@Bean`을 일일이 선언하지 않아도, `@Component`, `@Service`, `@Repository` 등이 붙은 클래스를 자동으로 찾아서 등록합니다.

---

## 6️⃣ 개념 정리

### IoC (제어의 역전)

카페를 생각해보겠습니다.

**IoC 전**: 손님이 직접 주방에 들어가서 커피를 만들어 가져옵니다.  
**IoC 후**: 손님은 주문만 하고, 바리스타(스프링 컨테이너)가 알아서 만들어 가져다 줍니다.

기존에는 객체가 스스로 의존 객체를 생성하고 제어했습니다.  
IoC 후에는 제어권이 프레임워크(스프링)로 넘어가고,  
객체는 자신의 역할만 수행합니다.

> 제어의 흐름이 개발자 코드 → 프레임워크로 역전됩니다.

---

### 싱글톤 (Singleton)

스프링은 기본적으로 빈을 **싱글톤**으로 관리합니다.

예를 들어 `NotificationService`를 10명의 사용자가 동시에 요청해도,  
스프링은 하나의 `NotificationService` 객체만 만들어두고 모두에게 같은 객체를 돌려줍니다.

비유하자면 **공유 복사기**입니다.  
회사에서 복사기를 사람마다 한 대씩 사는 게 아니라,  
한 대를 공유해서 씁니다. 상태 없이 기능만 수행하는 객체에 적합합니다.

단, 싱글톤 객체는 **공유 상태(필드에 값을 저장)를 가지면 안 됩니다**.  
여러 요청이 동시에 접근하면 값이 뒤섞일 수 있습니다.
