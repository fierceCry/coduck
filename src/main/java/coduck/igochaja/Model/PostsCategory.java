package coduck.igochaja.Model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Document(collection = "posts_category") // 게시글 카테고리
public class PostsCategory {
    @Id
    private String id;
    private String name;
    // getter, setter, constructors
}
