package coduck.igochaja.Repository;

import coduck.igochaja.Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    default User saveUser(String socialId, String name, String email, String social, String image) {
        return save(new User(socialId, name, email, social, image));
    }

    public User findByEmail(String email);
}