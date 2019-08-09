package weblog;

public interface IUserService {
    User registerNewUserAccount(UserDto accountDto) throws EmailExistsException;
}
