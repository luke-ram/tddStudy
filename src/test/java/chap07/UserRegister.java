package chap07;

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
