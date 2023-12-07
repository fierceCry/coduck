package coduck.igochaja.Model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Document(collection = "follow") // 팔로잉 / 팔로워
public class Follow {
    private Date createdAt;
    private Date updatedAt;
    @Id
    private String id;
    @DBRef
    private User loginUser;
    @DBRef
    private User followUser;
    // getter, setter, constructors
}