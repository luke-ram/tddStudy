<h3>암호 검사기</h3>

1. 길이가 8글자 이상
2. 0부터 9 사이의 숫자를 포함
3. 대문자 포함
4. 세 규칙을 모두 충족하면 암호는 강함이다.
5. 2개의 규칙을 충족하면 암호는 보통이다.
6. 1개 이하의 규칙을 충족하면 암호는 약함이다.

> 첫 번째 테스트를 선택할 때는 가장 쉽거나 가장 예외적인 상황을 선택하자

* 가장 단순한 구조의 테스트를 먼저 작성

```java
    public class PasswordStrengthMeterTest {

    @Test
    void meetsAllCriteria_Then_Strong() {
        PasswordStrengthMeter meter = new PasswordStrengthMeter();
        PasswordStrength result = meter.meter("ab12!@AB");
        Assertions.assertEquals(PasswordStrength.STRONG, result);

    }
}
```

* 그리고 컴파일 에러나는 것을 먼저 해결(클래스 생성..)

```java
public class PasswordStrengthMeter {
    public PasswordStrength meter(String s) {
        return null;
    }
}
```

* 가장 빠르게 테스트 통과할 수 있도록 코드 수정
```java
public class PasswordStrengthMeter {
    public PasswordStrength meter(String s) {
        return PasswordStrength.STRONG;
    }
}
```

* 두번째 검증 대상 추가
```java
public class PasswordStrengthMeterTest {

    @Test
    void meetsAllCriteria_Then_Strong(){
        PasswordStrengthMeter meter = new PasswordStrengthMeter();
        PasswordStrength result = meter.meter("ab12!@AB");
        Assertions.assertEquals(PasswordStrength.STRONG, result);

        PasswordStrength result2 = meter.meter("abc1!Add");
        Assertions.assertEquals(PasswordStrength.STRONG, result2);

    }
}
```

* 테스트 성공 확인 후 다른케이스의 검증 추가(암호가 7글자 미만)

```java
public class PasswordStrengthMeterTest {
    @Test
    void meetsOtherCriteria_except_for_Length_Then_Normal(){
        PasswordStrengthMeter meter = new PasswordStrengthMeter();
        PasswordStrength result = meter.meter("ab12!@A");
        Assertions.assertEquals(PasswordStrength.NORMAL, result);
    }
}
```
* 컴파일 오류나는 NORMAL enum타입을 추가

```java
public enum PasswordStrength {
    STRONG, NORMAL
}
```

* 코드를 아래와 같이 수정시 첫번째 테스트 케이스에서 에러가 발생, 즉 이 방식으로 처리하면 안된다.
```java
public class PasswordStrengthMeter {
    public PasswordStrength meter(String s) {
        return PasswordStrength.NORMAL;
    }
}
```

* 가장 빠르게 테스트를 통과 할 수 있는 방법으로 코드를 수정한다. 
```java
public class PasswordStrengthMeter {
    public PasswordStrength meter(String s) {
        if (s.length() < 8) {
            return PasswordStrength.NORMAL;
        }
        return PasswordStrength.STRONG;
    }
}
```

* 또 다른 테스트 케이스 추가
```java
public class PasswordStrengthMeterTest {
    
    @Test
    void meetsOtherCriteria_except_for_number_Then_Normal(){
        PasswordStrengthMeter meter = new PasswordStrengthMeter();
        PasswordStrength result = meter.meter("ab!@ABqwer");
        Assertions.assertEquals(PasswordStrength.NORMAL, result);
    }
}
```

* 소스코드 수정
```java
public class PasswordStrengthMeter {
    public PasswordStrength meter(String s) {
        if (s.length() < 8) {
            return PasswordStrength.NORMAL;
        }

        boolean containsNum = false;
        for (char ch : s.toCharArray()) {
            if (ch >= '0' && ch <= '9') {
                containsNum = true;
                break;
            }
        }

        if (!containsNum) return PasswordStrength.NORMAL;

        return PasswordStrength.STRONG;
    }
}
```

* 코드 리팩토링 한번 (메소드 추출)
```java
public class PasswordStrengthMeter {
    public PasswordStrength meter(String s) {
        if (s.length() < 8) {
            return PasswordStrength.NORMAL;
        }

        boolean containsNum = meetsContainingNumberCriteria(s);

        if (!containsNum) return PasswordStrength.NORMAL;

        return PasswordStrength.STRONG;
    }

    private boolean meetsContainingNumberCriteria(String s) {
        for (char ch : s.toCharArray()) {
            if (ch >= '0' && ch <= '9') {
                return true;
            }
        }
        return false;
    }
}
```

* 테스트 코드도 리팩토링
```java
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

    private void assertStrength(String s, PasswordStrength normal) {
        PasswordStrength result = meter.meter(s);
        Assertions.assertEquals(normal, result);
    }
}
```

* 공백에 대한 테스트 추가
```java
public class PasswordStrengthMeterTest {
    @Test
    void emptyInput_Then_Invalid(){
        assertStrength("", PasswordStrength.INVALID);
    }
}
```

* INVALID타입 추가
```java
public enum PasswordStrength {
    STRONG, NORMAL, INVALID
}
```

* meter 메서드 수정
```java
public class PasswordStrengthMeter {
    public PasswordStrength meter(String s) {
        
        if (s.isEmpty()) return PasswordStrength.INVALID;

        if (s.length() < 8) {
            return PasswordStrength.NORMAL;
        }

        boolean containsNum = meetsContainingNumberCriteria(s);

        if (!containsNum) return PasswordStrength.NORMAL;

        return PasswordStrength.STRONG;
    }

    private boolean meetsContainingNumberCriteria(String s) {
        for (char ch : s.toCharArray()) {
            if (ch >= '0' && ch <= '9') {
                return true;
            }
        }
        return false;
    }
}
```


* null에 대한 테스트 추가
```java
public class PasswordStrengthMeterTest {
    @Test
    void nullInput_Then_Invalid(){
        assertStrength(null, PasswordStrength.INVALID);
    }
}
```

* meter 메서드 수정
```java
public class PasswordStrengthMeter {
    public PasswordStrength meter(String s) {
        
        if (s == null || s.isEmpty()) return PasswordStrength.INVALID;

        if (s.length() < 8) {
            return PasswordStrength.NORMAL;
        }

        boolean containsNum = meetsContainingNumberCriteria(s);

        if (!containsNum) return PasswordStrength.NORMAL;

        return PasswordStrength.STRONG;
    }

    private boolean meetsContainingNumberCriteria(String s) {
        for (char ch : s.toCharArray()) {
            if (ch >= '0' && ch <= '9') {
                return true;
            }
        }
        return false;
    }
}
```

* 비밀번호 대문자 누락에 대한 테스트 케이스 추가
```java
public class PasswordStrengthMeterTest {
    @Test
    void meetsOtherCriteria_except_for_Uppercase_then_Normal(){
        assertStrength("ab12!@df", PasswordStrength.NORMAL);
    }
}
```

* 해당 로직에 대한 수정 코드 추가
```java
public class PasswordStrengthMeter {

    public PasswordStrength meter(String s) {

        if (s == null || s.isEmpty()) return PasswordStrength.INVALID;

        if (s.length() < 8) {
            return PasswordStrength.NORMAL;
        }

        boolean containsNum = meetsContainingNumberCriteria(s);
        if (!containsNum) return PasswordStrength.NORMAL;

        boolean containsUpperCase = false;
        for (char ch : s.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                containsUpperCase =  true;
            }
        }
        if(!containsUpperCase) return PasswordStrength.NORMAL;

        return PasswordStrength.STRONG;
    }
}
```

* 리펙토링
```java
public class PasswordStrengthMeter {

    public PasswordStrength meter(String s) {

        if (s == null || s.isEmpty()) return PasswordStrength.INVALID;

        if (s.length() < 8) {
            return PasswordStrength.NORMAL;
        }

        boolean containsNum = meetsContainingNumberCriteria(s);
        if (!containsNum) return PasswordStrength.NORMAL;

        boolean containsUpperCase = isContainsUpperCase(s);
        if(!containsUpperCase) return PasswordStrength.NORMAL;

        return PasswordStrength.STRONG;
    }

    private boolean isContainsUpperCase(String s) {
        boolean containsUpperCase = false;
        for (char ch : s.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                containsUpperCase =  true;
            }
        }
        return containsUpperCase;
    }

    private boolean meetsContainingNumberCriteria(String s) {
        for (char ch : s.toCharArray()) {
            if (ch >= '0' && ch <= '9') {
                return true;
            }
        }
        return false;
    }
}
```

* 다음 케이스 추가
```java
public class PasswordStrengthMeterTest {
    @Test
    void meetsOnlyLengthCriteria_Then_Weak(){
        assertStrength("abcdefghi", PasswordStrength.WEAK);
    }
}
```

* ENUM 타입 추가
```java
public enum PasswordStrength {
    STRONG, INVALID, WEAK, NORMAL
}
```
* 실행시 에러 발생 이 결과는 NORMAL이 리턴되며 실패하는데, 문자열 길이는 통과했지만, 숫자를 포함하고 있지 않아서 발생한 오류
* 길이는 만족했다는 것을 뒤쪽에서 확인할 수 있어야함


* method 수정
```java
public class PasswordStrengthMeter {

    public PasswordStrength meter(String s) {

        if (s == null || s.isEmpty()) return PasswordStrength.INVALID;
        
        boolean containsNum = meetsContainingNumberCriteria(s);
        boolean containsUpperCase = isContainsUpperCase(s);
        boolean lengthEnough = s.length() >= 8;

        if(lengthEnough && !containsNum && ! containsUpperCase)
            return PasswordStrength.WEAK;

        if (!lengthEnough) {
            return PasswordStrength.NORMAL;
        }
        if (!containsNum) return PasswordStrength.NORMAL;
        if(!containsUpperCase) return PasswordStrength.NORMAL;
        return PasswordStrength.STRONG;
    }
    
}
```

* 다음 케이스 추가
```java
public class PasswordStrengthMeterTest {
    @Test
    void meetsOnlyNumCriteria_Then_Weak(){
        assertStrength("12345", PasswordStrength.WEAK);
    }
}
```

* 메서드 수정
```java
public class PasswordStrengthMeter {

    public PasswordStrength meter(String s) {
    
        if (s == null || s.isEmpty()) return PasswordStrength.INVALID;

        boolean containsNum = meetsContainingNumberCriteria(s);
        boolean containsUpperCase = isContainsUpperCase(s);
        boolean lengthEnough = s.length() >= 8;

        if(lengthEnough && !containsNum && ! containsUpperCase)
            return PasswordStrength.WEAK;

        if(!lengthEnough && containsNum && !containsUpperCase)
            return PasswordStrength.WEAK;

        if (!lengthEnough) {
            return PasswordStrength.NORMAL;
        }
        if (!containsNum) return PasswordStrength.NORMAL;
        if(!containsUpperCase) return PasswordStrength.NORMAL;

        return PasswordStrength.STRONG;
    }
}
```

* 다음케이스 추가(대문자만 있는 케이스)
```java
public class PasswordStrengthMeterTest {
    @Test
    void meetsOnlyUpperCriteria_Then_Weak(){
        assertStrength("ABCDEF", PasswordStrength.WEAK);
    }
}
```

* 메서드 수정
```java
public class PasswordStrengthMeter {

    public PasswordStrength meter(String s) {

        if (s == null || s.isEmpty()) return PasswordStrength.INVALID;

        boolean containsNum = meetsContainingNumberCriteria(s);
        boolean containsUpperCase = isContainsUpperCase(s);
        boolean lengthEnough = s.length() >= 8;

        if (lengthEnough && !containsNum && !containsUpperCase)
            return PasswordStrength.WEAK;

        if (!lengthEnough && containsNum && !containsUpperCase)
            return PasswordStrength.WEAK;

        if (!lengthEnough && !containsNum && containsUpperCase)
            return PasswordStrength.WEAK;

        if (!lengthEnough) {
            return PasswordStrength.NORMAL;
        }
        if (!containsNum) return PasswordStrength.NORMAL;
        if (!containsUpperCase) return PasswordStrength.NORMAL;

        return PasswordStrength.STRONG;
    }
}
```

* 1차 리팩토링
```java
public class PasswordStrengthMeter {

    public PasswordStrength meter(String s) {

        if (s == null || s.isEmpty()) return PasswordStrength.INVALID;

        boolean containsNum = meetsContainingNumberCriteria(s);
        boolean containsUpperCase = isContainsUpperCase(s);
        boolean lengthEnough = s.length() >= 8;

        int metCounts = 0;
        if (lengthEnough) metCounts++;
        if (containsNum) metCounts++;
        if (containsUpperCase) metCounts++;
        if (metCounts == 1) return PasswordStrength.WEAK;
       
        if (!lengthEnough) {
            return PasswordStrength.NORMAL;
        }
        if (!containsNum) return PasswordStrength.NORMAL;
        if (!containsUpperCase) return PasswordStrength.NORMAL;

        return PasswordStrength.STRONG;
    }
}
```

* 2차 리팩토링
```java
public class PasswordStrengthMeter {

    public PasswordStrength meter(String s) {

        if (s == null || s.isEmpty()) return PasswordStrength.INVALID;

        boolean containsNum = meetsContainingNumberCriteria(s);
        boolean containsUpperCase = isContainsUpperCase(s);
        boolean lengthEnough = s.length() >= 8;

        int metCounts = 0;
        if (lengthEnough) metCounts++;
        if (containsNum) metCounts++;
        if (containsUpperCase) metCounts++;

        if (metCounts == 1) return PasswordStrength.WEAK;
        if(metCounts ==2) return PasswordStrength.NORMAL;
        
        return PasswordStrength.STRONG;
    }
}
```

* 아무것도 충족하지 않은 경우 테스트케이스 추가
```java
public class PasswordStrengthMeterTest {
    @Test
    void meetNoCritera_Then_Weak(){
        assertStrength("abc", PasswordStrength.WEAK);
    }
}
```

* 메소드 수정
```java
public class PasswordStrengthMeter {

    public PasswordStrength meter(String s) {

        if (s == null || s.isEmpty()) return PasswordStrength.INVALID;

        boolean containsNum = meetsContainingNumberCriteria(s);
        boolean containsUpperCase = isContainsUpperCase(s);
        boolean lengthEnough = s.length() >= 8;

        int metCounts = 0;
        if (lengthEnough) metCounts++;
        if (containsNum) metCounts++;
        if (containsUpperCase) metCounts++;

        if (metCounts <= 1) return PasswordStrength.WEAK;
        if(metCounts ==2) return PasswordStrength.NORMAL;
        
        return PasswordStrength.STRONG;
    }
}
```

* 가독성 리팩토링
```java
public class PasswordStrengthMeter {

    public PasswordStrength meter(String s) {

        if (s == null || s.isEmpty()) return PasswordStrength.INVALID;

        int metCounts = getMetCriteriaCounts(s);

        if (metCounts <= 1) return PasswordStrength.WEAK;
        if (metCounts == 2) return PasswordStrength.NORMAL;

        return PasswordStrength.STRONG;
    }

    private int getMetCriteriaCounts(String password) {

        int metCounts = 0;
        if (password.length() >= 8) metCounts++;
        if (meetsContainingNumberCriteria(password)) metCounts++;
        if (isContainsUpperCase(password)) metCounts++;
        return metCounts;
    }
}
```