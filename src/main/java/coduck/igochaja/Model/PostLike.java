package coduck.igochaja.Model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@ToString
@Document(collection = "post_like")
public class PostLike {
    @Id
    private String id;

    @DBRef
    private Post post;

    @DBRef
    private User user;
    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;
}

