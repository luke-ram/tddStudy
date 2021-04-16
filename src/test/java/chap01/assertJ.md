<h3> Assert 정리</h3>


```java
public class assertExample(){

        @Test
        public void testMethod(){
                org.junit.Assert.assertEquals("a","a"); //실제 값이 기대하는 값과 같은지 검사 
                org.junit.Assert.assertSame("a","a"); // 두객체가 동일한 객체인지 검사
                org.junit.Assert.assertNotSame("a","b"); //두 객체가 동일하지 않은 객체인지 검사한다.
                org.junit.Assert.assertTrue(true); //값이 true인지 검사한다.
                org.junit.Assert.assertFalse(false); //값이 false인지 검사한다.
                org.junit.Assert.assertNull(null); // 값이 null인지 검사한다.
                org.junit.Assert.assertNotNull("notNull"); //값이 null이 아닌지 검사한다.
                org.junit.Assert.fail(); // 테스트를 실패 처리한다.

        }       
}
```

<h3> AssertJ정리</h3>


```java
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Maps;import org.junit.jupiter.api.Test;
import java.util.Arrays;import java.util.HashMap;import java.util.Map;import java.util.regex.Pattern;import static org.assertj.core.api.Assertions.*;
    
public class assertJExample(){

    @Test
    public void primitiveType(){
        //assertThat(실제값).검증메서드(기대값)의 형태
          
        assertThat("test").isEqualTo("test"); //결과 값이 같은지 검증한다.
        assertThat("test").isNotEqualTo("test");
        assertThat("test").isNull(); //null인지 검증
        assertThat("test").isNotNull();//null이 아닌지 검증
        assertThat(Arrays.asList(1,2,3,4)).isIn(2,3); //값 목록에 포함되어 있는지 검증
        assertThat(Arrays.asList(1,2,3,4)).isNotIn(5); //값 목록에 포함되어 있는 않은지 검증

        //Comparable 인터페이스를 구현한 타입이나 int, double과 같은 숫자 타입의 경우 다음 메서드를 이용해서 값을 검증
        assertThat(1).isLessThan(2); //값(2)보다 작은지 검증
        assertThat(10).isLessThanOrEqualTo(10); // 값(10)보다 작거나 같은지 검증
        assertThat(5).isGreaterThan(3); //값(3)보다 큰지 검증한다.
        assertThat(5).isGreaterThanOrEqualTo(5); // 값보다(5) 크거나 같은지 검증
        assertThat(5).isBetween(1,6); //값1(1)과 값2(6) 사이에 포함되는지 검증

        assertThat(true).isTrue(); // true인지 검증
        assertThat(false).isFalse() // false인지 검증

        
    }
   
    @Test
    public void String에_대한_추가_검증_메서드(){
       assertThat("test").contains("t","e"); // 인자로 지정한 문자열들을 모두 포함하고 있는지 검증
       assertThat("test").containsOnlyOnce("te"); // 해당 문자열을 딱 한번만 포함하는지 검증
       assertThat("123").containsOnlyDigits(); //숫자만 포함하고 있는지 검증
       assertThat("ab cd").containsWhitespaces(); // 공백 문자를 포함하고 있는지 검증
       assertThat("  ").containsOnlyWhitespaces(); //공백 문자만 포함하는지 검증
       assertThat("abcd").doesNotContains("e"); //인자로 지정한 문자열들을 모두 포함하고 있지 않은지 검증.
       assertThat("abcd").doesNotContainAnyWhitespaces(); // 공백 문자를 포함하고 있지 않은지 검출
       assertThat("abcd").doesNotContainPattern(Pattern.compile("")); //정규 표현식에 일치하는 문자를 포함하고 있는지 검증
       assertThat("abcd").startsWith("a"); // 지정한 문자열로 시작하는지 검증
       assertThat("abcd").doesNotStartWith("e");//지정한 문자열로 시작하지 않는지 검증
       assertThat("abcd").endsWith("d"); // 지정한 문자열로 끝나는지를 검증한다.
       assertThat("abcd").doesNotEndWith("e"); // 지정한 문자열로 끝나는지 않는지 검증한다.
    }

    @Test
    public void 콜렉션에_대한_메서드_검증() {
        Assertions.assertThat(Arrays.asList(1,2,3)).hasSize(3); // 콜렉션 사이즈 크기 검증
        Assertions.assertThat(Arrays.asList(1,2,3)).contains(2,3); // 2,3을 포함하고 있는지 검증
        Assertions.assertThat(Arrays.asList(1,2,3)).containsOnly(2); // 콜렉션이 지정한 값만 포함하고 있는지 검증
        Assertions.assertThat(Arrays.asList(1,2,3)).containsAnyOf(2); // 콜렉션이 지정한 값 중 일부를 포함하는지 검증
        Assertions.assertThat(Arrays.asList(1,2,3)).containsOnlyOnce(2); // 콜렉션이 지정한 값을 한번만 포함하는지 검증
        
        //Map
        Map map  = new HashMap<>();
        map.put("a",1);                    
        map.put("b",2);                    
        Assertions.assertThat(map).containsKey("a"); // Map이 지정한 키를 포함하는지 검증
        Assertions.assertThat(map).containsKeys("a","b"); // Map이 지정한 키들을 포함하는지 검증
        Assertions.assertThat(map).containsOnlyKeys("a","b"); // Map이 지정한 키만 포함하는지 검증
        Assertions.assertThat(map).doesNotContainKey("c"); // Map이 지정한 키들을 포함하지 않아야 한다.
        Assertions.assertThat(map).containsValues("c","d"); // Map이 지정한 값들을 포함하지 않는다.
        Assertions.assertThat(map).contains(map); // Map이 지정한 Entry<K,V>를 포함하는지 검증한다.
    }   

    @Test
    public void 익셉션_대한_메서드_검증() {
        assertThatThrownBy(() -> 1/0).isExactlyInstanceOf(IllegalAccessError.class);
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> 1/0);
        Assertions.assertThatIllegalArgumentException().isThrownBy(() -> 1/0);
        assertThatCode(() -> 2/2).doesNotThrowAnyException();



        
    
    }   

}
```
