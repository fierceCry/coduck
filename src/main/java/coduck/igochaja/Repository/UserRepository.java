package coduck.igochaja.Repository;

import coduck.igochaja.Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    default User saveUser(String id, String name, String email, String social, String image) {
        return save(new User(id, name, email, social, image));
    }
}