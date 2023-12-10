package coduck.igochaja.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import coduck.igochaja.Model.Login;

public interface LoginRepository extends MongoRepository<Login, String> {
    Login findByEmailAndPassword(String email, String password);
}

