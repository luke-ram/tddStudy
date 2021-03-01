package chap07;

import chap07.notifier.EmailNotifier;
import chap07.notifier.SpyEmailNotifier;
import chap07.user.MemoryUserRepository;
import chap07.user.User;
import chap07.user.UserRepository;
import chap07.validation.DuplicateExcetpion;
import chap07.validation.WeakPasswordChecker;
import chap07.validation.WeakPasswordException;

public class UserRegister {
    private WeakPasswordChecker passwordChecker;
    private UserRepository userRepository;
    private EmailNotifier emailNotifier;

    public UserRegister(WeakPasswordChecker passwordChecker, MemoryUserRepository fakeRepository, SpyEmailNotifier spyEmailNotifier) {
        this.passwordChecker = passwordChecker;
        this.userRepository = fakeRepository;
        this.emailNotifier = spyEmailNotifier;
    }


    public void register(String id, String pw, String email) {
        //구현전
        if (passwordChecker.checkPasswordWeak(pw)) {
            throw new WeakPasswordException();
        }

        User user = userRepository.findById(id);
        if (user != null) {
            throw new DuplicateExcetpion();
        }
        userRepository.save(new User(id, pw, email));
        emailNotifier.sendRegisterEmail(email);

    }
}
