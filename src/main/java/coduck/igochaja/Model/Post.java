package coduck.igochaja.Model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Document(collection = "posts") // 게시글
public class Post {
    @Id
    private String id;
    private String title;
    private String content;
    private List<String> images;
    private List<String> tags;
    private Date createdAt;
    private Date updatedAt;
    @DBRef
    private User user;
    @DBRef
    private PostsCategory postCategory;
    // getter, setter, constructors
}
