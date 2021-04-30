package appendix;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

public class GameGenMockTest {

    @Test
    void mockTest() {
        GameNumGen genMock = mock(GameNumGen.class);
        given(genMock.generate(GameLevel.EASY)).willReturn("easy");

        String generate = genMock.generate(GameLevel.EASY);
        assertEquals("easy", generate);
    }

    @Test
    void mockExcetpion() {
        GameNumGen genMock = mock(GameNumGen.class);
        given(genMock.generate(GameLevel.EASY)).willThrow(new IllegalArgumentException());
        assertThrows(
                IllegalArgumentException.class, () -> genMock.generate(GameLevel.EASY)
        );
    }

    @Test
    void returnTypeVoidTest() {
        List<String> mockList = mock(List.class);
        willThrow(UnsupportedOperationException.class)
                .given(mockList).clear();
        assertThrows(
                UnsupportedOperationException.class, () -> mockList.clear()
        );
    }

    @Test
    void anyTypeTest() {
        GameNumGen genMock = mock(GameNumGen.class);
        given(genMock.generate(any())).willReturn("456");

        String num = genMock.generate(GameLevel.EASY);
        assertEquals("456", num);

        String num2 = genMock.generate(GameLevel.NORMAL);
        assertEquals("456", num2);
    }

    @Test
    void matchParameterType() {
        List<String> mockList = mock(List.class);
        given(mockList.set(anyInt(), "123")).willReturn("456"); // 안됨
        given(mockList.set(anyInt(), eq("123"))).willReturn("456"); // 가능
    }

    @Test
    void actionTest(){
        GameNumGen genMock = mock(GameNumGen.class);
        Game game = new Game(genMock);
        game.init(GameLevel.EASY);

        then(genMock).should().generate(GameLevel.EASY);
        //정확한 값이 아니라 메서드가 불렸는지 여부가 중요할때
        then(genMock).should().generate(any());
        //정확하게 한 번만 호출된 것을 검증하고 싶을때
        then(genMock).should(only()).generate(any());
    }


}
