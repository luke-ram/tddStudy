package chap07;

public class UserRegister {
    private WeakPasswordChecker passwordChecker;
    private UserRepository userRepository;

    public UserRegister(WeakPasswordChecker passwordChecker, MemoryUserRepository fakeRepository) {
        this.passwordChecker = passwordChecker;
        this.userRepository = fakeRepository;
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
        userRepository.save(new User(id,pw,email));

    }
}
