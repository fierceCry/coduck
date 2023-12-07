package coduck.igochaja.Model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Document(collection = "posts_comment") //게시글 댓글
public class PostsComment {
    private String content;
    @Id
    private String id;
    @DBRef
    private User user;
    @DBRef
    private Post post;
    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;
    // getter, setter, constructors
}

