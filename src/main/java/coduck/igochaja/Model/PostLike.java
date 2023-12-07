package coduck.igochaja.Model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Document(collection = "post_like") // 게시글 좋아요
public class PostLike {
    @Id
    private String id;

    @DBRef
    private Post post;

    @DBRef
    private User user;

    private Date createdAt;
    // getters, setters, constructors
}

