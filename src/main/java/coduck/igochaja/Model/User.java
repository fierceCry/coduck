package coduck.igochaja.Model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.CreatedDate;
import java.util.Date;

@Getter
@Setter
@ToString // toString 메소드 자동 생성
@Document(collection = "users") //유저
public class User {
    @Id
    private String id;
    private String email;
    private String password;
    private String nickName;
    private String social;
    private String report;
    private String image;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    public User(String id, String nickName, String email, String social, String image) {
        this.id = id;
        this.nickName = nickName;
        this.email = email;
        this.social = social;
        this.image = image;
    }
}
