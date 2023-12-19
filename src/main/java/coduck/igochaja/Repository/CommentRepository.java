package coduck.igochaja.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import coduck.igochaja.Model.Comment;

public interface CommentRepository extends MongoRepository<Comment, String> {

}
