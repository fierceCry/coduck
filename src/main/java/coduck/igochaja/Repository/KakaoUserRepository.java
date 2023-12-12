package coduck.igochaja.Repository;

import coduck.igochaja.Model.KakaoUser;
import coduck.igochaja.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.HashMap;
import java.util.Optional;

@Repository
public interface KakaoUserRepository extends MongoRepository<User, String> {

}
