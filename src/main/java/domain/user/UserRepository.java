package domain.user;

import errors.UserNotFoundException;

import java.util.List;

public interface UserRepository {
	String create(NewUser user);
    List<User> getUsers();
    void deleteUser(String id) throws UserNotFoundException;
    User updateUser(User user) throws UserNotFoundException;
}