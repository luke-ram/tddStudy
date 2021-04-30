package chap07self;

public class AutoDebitRegister {
    private CardNumberValidator validator;


    public AutoDebitRegister(CardNumberValidator validator) {
        this.validator = validator;
    }

    public String register(AutoDebitReq req) {
        CardValidity validity = validator.validate(req.getCardNumber());
        if (validity == CardValidity.VALID) {
            return "ok";
        } else if(validity == CardValidity.THEFT){
            return "theft";
        }else{
            return "error";
        }

    }
}
