package coduck.igochaja.Service;

import coduck.igochaja.Model.User;
import coduck.igochaja.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUserByEmail(String email, String social) {return userRepository.findByEmail(email, social);}
}
