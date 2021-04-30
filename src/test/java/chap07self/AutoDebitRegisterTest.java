package chap07self;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class AutoDebitRegisterTest {

    @Test
    void successTest(){
        CardNumberValidator mock = mock(CardNumberValidator.class);
        BDDMockito.given(mock.validate("1234")).willReturn(CardValidity.VALID);
        AutoDebitRegister autoDebitRegister = new AutoDebitRegister(mock);
        String register = autoDebitRegister.register(new AutoDebitReq("1234"));
        Assertions.assertEquals("ok",register);

    }

    @Test
    void theftTest(){
        CardNumberValidator mock = mock(CardNumberValidator.class);
        BDDMockito.given(mock.validate("1234")).willReturn(CardValidity.THEFT);
        AutoDebitRegister autoDebitRegister = new AutoDebitRegister(mock);
        String register = autoDebitRegister.register(new AutoDebitReq("1234"));
        Assertions.assertEquals("theft",register);

    }

}