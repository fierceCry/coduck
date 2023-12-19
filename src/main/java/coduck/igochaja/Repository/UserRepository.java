package coduck.igochaja.Repository;


import coduck.igochaja.Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {

    default User saveUser(String socialId, String name, String email, String social, String image) {
        return save(new User(socialId, name, email, social, image));
    }

    public User findByEmail(String email, String social);

    default User ProfileImage(String fileUrl, String objectId) {
        return findById(objectId)
                .map(user -> {
                    user.setImage(fileUrl);
                    return save(user);
                })
                .orElse(null);
    }
}