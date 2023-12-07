package coduck.igochaja.Model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "posts_category") // 게시글 카테고리
public class PostsCategory {
    @Id
    private String id;
    private String name;
    // getter, setter, constructors
}
