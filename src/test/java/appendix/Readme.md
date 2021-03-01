<h3>Mockito</h3>

- 모의객체 생성하는 방법

```java
    @Test
    void mockTest(){
            GameNumGen genMock=mock(GameNumGen.class);
    }
```

- 스텁 생성
```java
    GameNumGen genMock = mock(GameNum.class);
    given(genMock.generate(GameLevel.EASY)).willReturn("123");
```

- 지정하는 값을 리턴하는 대신 익셉션을 발생시킬시
```java
   given(genMock.generate(GameLevel.EASY)).willThrow(IllegalArgumentException());
   assertThrows(
           IllegalArgumentExcetion.class,() -> genMock.generate(null)
    );
```

- 리턴 타입이 void인 메서드에 대해 익셉션 발생시 BDDMockito.willThrow()메서드로 시작
```java
    List<String> mockList = mock(List.class)
    willThrow(UnsupportedOperationException.class)
    .given(mockList)
    .clear();

    assertThrows(
        UnsupportedOperation.class,() -> mockList.clear()
    )
```
1. BDDMockito.willThrow()메소드는 발생할 익셉션 타입이나 익셉션 객체를 인자로 받는다. <br>
2. given()메서드는 모의 객체를 전달받는다. 모의 객체의 메서드 실행이 아닌 모의 객체임에 유의할것<br>
3. given()메서드는 인자로 전달받은 모의 객체 자신을 리턴하는데 이때 익셉션을 발생할 메서드를 호출한다.<br>
4. clear()에서 실제로 모의 객체의 메서드를 호출하지 않는다. 단지 익셉션을 발생할 모의 객체를 설정하는것뿐이다.


<h3> 인자 매칭 정리</h3>

> Mockito는 일치하는 스텁 설정이 없을 경우 리턴 타입의 기본 값을 리턴한다.<br>
> 리턴 타입 int -> 0 <br>
> 리턴 타입 boolean -> false <br>
> String이나, List와 같은 참조타입 -> null

```java
    GameNumGen genMock = mock(GameNumGen.class);
    given(genMock.generate(any())).willReturn("456");
    
    String num = genMock.generate(GameLevel.EASY);
    assertEquals("456", num);

    String num2 = genMock.generate(GameLevel.NORMAL);
    assertEquals("456", num2);
```

- 종류
    - anyInt(), anyShort(), anyLong(), anyByte(), anyChar(), anyDouble(), anyFloat(),anyBoolean()
        : 기본 데이터 타입에 대한 임의 값 일치
    - anyString(): 문자열에 대한 임의 값 일치
    - any() : 임의 타입에 대한 일치
    - anyList(), anySet(), anyMap(), anyCollection() : 임의 콜렉션에 대한 일치
    - matches(String), matches(Pattern): 정규 표현식을 이용한 String값 일치 여부
    - eq(값) : 특정 값과 일치 여부



- 임의의 값과 일치하는 인자와 정확하게 일치하는 인자를 함께 사용할때 아래와 같이 사용해야함
```java
    given(MockList.set(anyInt()), "123")).willReturn("456"); // 안됨
    given(MockList.set(anyInt()), eq("123"))).willReturn("456"); // 가능
```

> ArgumentMatchers의 anyInt()나 any()등의 메서드는 내부적으로 인자의 일치 여부를 판단하기 위해 ArgumentMatcher를 등록한다.
> Mockito는 한 인자라도 ArgumentMatcher를 사용해서 설정한 경우 모든 인자를 ArgumentMatcher를 이용해서 설정하고 있다.


<h3>행위 검증</h3>
- 메서드 호출 횟수를 검증하기 위해 제공하는 메서드
    - only(): 한번만 호출
    - times(int): 지정한 횟수만큼 호출
    - never(): 호출하지 않음
    - atLeast(int): 적어도 지정한 횟수만큼 호출
    - atLeastOnce(): atLeast(1)과 동일
    - atMost(int): 최대 지정한 횟수만큼 호출
    
```java
    GameNumGen genMock = mock(GameNumGen.class);
    Game game = new Game(genMock);
    game.init(GameLevel.EASY);
    
    then(genMock).should().generate(GameLevel.EASY);
    //정확한 값이 아니라 메서드가 불렸는지 여부가 중요할때
    then(genMock).should().generate(any());
    //정확하게 한 번만 호출된 것을 검증하고 싶을때
    then(genMock).should(only()).generate(any());
```
1. should() : 다음에 실제로 불려야 할 메서드를 지정한다. <br>
   (ex) generate메서드가 호출되어야 한다.

<h3>Junit5 확장 설정</h3>

```xml
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-junit-jupiter</artifactId>
        <version>3.6.28</version>
        <scope>test</scope>
    </dependency>
```
- <code> @Mock</code> 애노테이션을 붙인 필드에 대해 자동으로 모의 객체를 생성해준다.

```java
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

@ExtendWith(MockitoExtension.class)
public class Junit5ExtensionTest {

    @Mock
    private GameNumGen genMock;
}
```