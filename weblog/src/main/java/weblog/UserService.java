package weblog;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository repository; 
     
    @Transactional
    @Override
    public User registerNewUserAccount(UserDto accountDto) throws EmailExistsException {
         
        if (emailExist(accountDto.getEmail())) {  
            throw new EmailExistsException(
              "There is an account with that email adress: "
              +  accountDto.getEmail());
        }
        User user = new User();    
        user.setUsername(accountDto.getUsername());
        user.setPassword(accountDto.getPassword());
        user.setEmail(accountDto.getEmail());
        //user.setRoles(Arrays.asList("ROLE_USER"));
        
        return repository.save(user);  
        
    }
    private boolean emailExist(String email) {
        User user = repository.findByEmail(email);
        if (user != null) {
            return true;
        }
        return false;
    }
}