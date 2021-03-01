package chap07;

import chap07.notifier.EmailNotifier;
import chap07.notifier.SpyEmailNotifier;
import chap07.user.MemoryUserRepository;
import chap07.user.User;
import chap07.validation.DuplicateExcetpion;
import chap07.validation.StubWeakPasswordChecker;
import chap07.validation.WeakPasswordChecker;
import chap07.validation.WeakPasswordException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserRegisterTest {
    private UserRegister userRegister;
    private WeakPasswordChecker mockPasswordChecker = Mockito.mock(WeakPasswordChecker.class);

    private MemoryUserRepository fakeRepository = new MemoryUserRepository();
    private EmailNotifier mockEmailNotifier = Mockito.mock(EmailNotifier.class);

    @BeforeEach
    void setUp() {
        userRegister = new UserRegister(mockPasswordChecker, fakeRepository, mockEmailNotifier);

    }

    @DisplayName("이미 같은 ID가 존재하면 가입 실패 =")
    @Test
    void dupIdExists() {
        fakeRepository.save(new User("id", "pw1", "email@eamil.com"));

        assertThrows(DuplicateExcetpion.class, () -> {
            userRegister.register("id", "pw", "email");
        });
    }

    @DisplayName("같은 ID가 없으면 가입 성공함")
    @Test
    void noDupId_RegisterSuccess() {
        userRegister.register("id", "pw", "email");

        User savedUser = fakeRepository.findById("id");
        assertEquals("id", savedUser.getId());
        assertEquals("email", savedUser.getEmail());
    }


    @DisplayName("약한 암호면 가입 실패")
    @Test
    void weakPassword() {

        BDDMockito.given(mockPasswordChecker.checkPasswordWeak("pw"))
                .willReturn(true);

        assertThrows(WeakPasswordException.class, () -> {
            userRegister.register("id", "pw", "email");
        });
    }

    @DisplayName("가입하면 메일을 전송함")
    @Test
    void whenRegisterThenSendMail() {
        userRegister.register("id", "pw", "email@email.com");
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        BDDMockito.then(mockEmailNotifier)
                .should()
                .sendRegisterEmail(captor.capture());

        String realEmail = captor.getValue();
        Assertions.assertEquals("email@email.com", realEmail);

    }

    @DisplayName("회원 가입시 암호 검사 수행함")
    @Test
    void checkPassword(){
        userRegister.register("id","pw","email");

        BDDMockito.then(mockPasswordChecker)
                .should()
                .checkPasswordWeak(BDDMockito.anyString());
    }
}
