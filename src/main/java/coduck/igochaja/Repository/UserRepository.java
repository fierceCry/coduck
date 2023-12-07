package coduck.igochaja.Repository;

import coduck.igochaja.Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    // 추가적인 쿼리 메서드 정의 가능
}
