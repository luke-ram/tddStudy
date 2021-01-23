package chap03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExpiryDateCalculatorTest {


    public static final ExpiryDateCalculator CAL = new ExpiryDateCalculator();

    @Test
    void 만원_납부하면_한달_뒤가_만료일이_됨() {

        assertExpiryDate(PayData.builder()
                        .billingDate(LocalDate.of(2021, 01, 23))
                        .payAmount(10_000)
                        .build(),
                LocalDate.of(2021, 02, 23));

        assertExpiryDate(PayData.builder()
                        .billingDate(LocalDate.of(2021, 05, 05))
                        .payAmount(10_000)
                        .build(),
                LocalDate.of(2021, 06, 05));
    }

    @Test
    @DisplayName("납부일과 한달 뒤 일자가 같지 않음")
    void not_match_expiryDate_one_Month() {

        assertExpiryDate(PayData.builder()
                        .billingDate(LocalDate.of(2019, 01, 31))
                        .payAmount(10_000)
                        .build(),
                LocalDate.of(2019, 02, 28));

        assertExpiryDate(PayData.builder()
                        .billingDate(LocalDate.of(2019, 05, 31))
                        .payAmount(10_000)
                        .build(),
                LocalDate.of(2019, 06, 30));

        assertExpiryDate(PayData.builder()
                        .billingDate(LocalDate.of(2020, 01, 31))
                        .payAmount(10_000)
                        .build(),
                LocalDate.of(2020, 02, 29));
    }

    @Test
    @DisplayName("첫 납부일과 만료일 일자가 다를때 만원 납부")
    void firstBillingDate_payBillingDate_is_Different() {
        PayData payData = PayData.builder()
                .firstBillingDate(LocalDate.of(2019, 01, 31))
                .billingDate(LocalDate.of(2019, 02, 28))
                .payAmount(10_000)
                .build();

        assertExpiryDate(payData, LocalDate.of(2019, 03, 31));

        PayData payData2 = PayData.builder()
                .firstBillingDate(LocalDate.of(2019, 01, 30))
                .billingDate(LocalDate.of(2019, 02, 28))
                .payAmount(10_000)
                .build();

        assertExpiryDate(payData2, LocalDate.of(2019, 03, 30));

        PayData payData3 = PayData.builder()
                .firstBillingDate(LocalDate.of(2019, 05, 31))
                .billingDate(LocalDate.of(2019, 06, 30))
                .payAmount(10_000)
                .build();

        assertExpiryDate(payData3, LocalDate.of(2019, 07, 31));

    }

    @Test
    @DisplayName("이만원 이상 납부하면 비례해서 만료일 계산")
    void pay_money_more_10_000() {
        assertExpiryDate(PayData.builder()
                        .billingDate(LocalDate.of(2019, 03, 01))
                        .payAmount(20_000).build(),
                LocalDate.of(2019, 05, 01));

        assertExpiryDate(PayData.builder()
                        .billingDate(LocalDate.of(2019, 03, 01))
                        .payAmount(30_000).build(),
                LocalDate.of(2019, 06, 01));
    }

    @Test
    @DisplayName("첫 납부일과 만료일 일자가 다를때 이만원 이상 납부")
    void firstBillingDate_payBillingDate_is_Different_payAmount_than_20_000() {
        assertExpiryDate(PayData.builder()
                        .firstBillingDate(LocalDate.of(2019, 01, 31))
                        .billingDate(LocalDate.of(2019, 02, 28))
                        .payAmount(20_000)
                        .build(),
                LocalDate.of(2019, 04, 30));

        assertExpiryDate(PayData.builder()
                        .firstBillingDate(LocalDate.of(2019, 03, 31))
                        .billingDate(LocalDate.of(2019, 04, 30))
                        .payAmount(30_000)
                        .build(),
                LocalDate.of(2019, 07, 31));

    }

    @Test
    @DisplayName("십만원을 나부하면 1년 제공")
    void payAmount_100_000_then_Add_one_Year() {
        assertExpiryDate(PayData.builder()
                        .billingDate(LocalDate.of(2019, 01, 28))
                        .payAmount(100_000)
                        .build(),
                LocalDate.of(2020, 01, 28));
    }


    private void assertExpiryDate(PayData payData, LocalDate expectedExpiryDate) {
        LocalDate expireDate = CAL.calculateExpiryDate(payData);
        assertEquals(expectedExpiryDate, expireDate);
    }
}
