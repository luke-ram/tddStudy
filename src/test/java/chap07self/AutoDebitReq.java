package chap07self;


public class AutoDebitReq {
    private String cardNumber;

    public String getCardNumber() {
        return cardNumber;
    }

    public AutoDebitReq(String cardNumber) {
        this.cardNumber = cardNumber;
    }
}
