package chap02self;

import chap02.PasswordStrength;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PasswordStrengthMeterTest {

    private PasswordStrengthMeter meter = new PasswordStrengthMeter();

    @Test
    void meetsAllCriteria_Then_Strong() {
        assertStrength("ab12!@AB", PasswordStrength.STRONG);
        assertStrength("abc1!Add", PasswordStrength.STRONG);
    }

    @Test
    void meetsOtherCriteria_except_for_Length_Then_Normal() {
        assertStrength("ab12!@A", PasswordStrength.NORMAL);
    }

    @Test
    void meetsOtherCriteria_except_for_number_Then_Normal() {
        assertStrength("ab!@ABqwer", PasswordStrength.NORMAL);
    }

    @Test
    void nullInput_Then_Invalid() {
        assertStrength(null, PasswordStrength.INVALID);
    }

    @Test
    void emptyInput_Then_Invalid() {
        assertStrength("", PasswordStrength.INVALID);
    }

    @Test
    void meetsOtherCriteria_except_for_Uppercase_then_Normal() {
        assertStrength("ab12!@df", PasswordStrength.NORMAL);
    }

    @Test
    void meetsOnlyLengthCriteria_Then_Weak() {
        assertStrength("abcdefghi", PasswordStrength.WEAK);
    }

    @Test
    void meetsOnlyNumCriteria_Then_Weak(){
        assertStrength("12345", PasswordStrength.WEAK);
    }

    @Test
    void meetsOnlyUpperCriteria_Then_Weak(){
        assertStrength("ABCDEF", PasswordStrength.WEAK);
    }

    @Test
    void meetNoCritera_Then_Weak(){
        assertStrength("abc", PasswordStrength.WEAK);
    }

    private void assertStrength(String s, PasswordStrength normal) {
        PasswordStrength result = meter.meter(s);
        Assertions.assertEquals(normal, result);
    }

    @Test
    public void assertJTest() {
        Assertions.assertEquals("a", "a");
    }

}
