package coduck.igochaja.Repository;

import coduck.igochaja.Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    // 기본적으로 제공되는 save 메서드 사용
    default User saveUser(String id, String name, String email, String social) {
        User user = new User();
        user.setId(id);
        user.setNickName(name);
        user.setEmail(email);
        user.setSocial(social);

        return save(user);
    }
}

