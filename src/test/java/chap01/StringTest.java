package chap01;

import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class StringTest {
    @Test
    public void sample(){
        Assertions.assertThat(Arrays.asList(1,2,3)).containsKey();
    }
}
