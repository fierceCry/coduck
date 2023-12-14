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
import java.util.List;

@Getter
@Setter
@ToString
@Document(collection = "posts") // 게시글
public class Post {
    @Id
    private String id;
    private String title;
    private String content;
    private List<String> images;
    private List<String> tags;
    @DBRef
    private User user;
    @DBRef
    private PostsCategory postCategory;
    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;
    // getter, setter, constructors
}
