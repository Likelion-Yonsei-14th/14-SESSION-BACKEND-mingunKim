public class Payment {
    private String type;
    private int amount;

    Payment(String type, int amount) {
        this.type = type;
        this.amount = amount;
    }

    public void pay() {
        if (amount <= 0) {
            System.out.println("금액이 올바르지 않습니다.");
            return;
        }
        processType();
    }

    private void processType() {
        if (type.equals("card")) {
            System.out.println("카드 결제: " + amount);
        } else if (type.equals("kakao")) {
            System.out.println("카카오페이 결제: " + amount);
        } else {
            System.out.println("지원하지 않는 결제 방식");
        }
    }
}
