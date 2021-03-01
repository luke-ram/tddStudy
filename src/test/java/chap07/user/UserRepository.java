package chap07.user;

import chap07.user.User;

public interface UserRepository {
    void save(User user);

    User findById(String id);

}
