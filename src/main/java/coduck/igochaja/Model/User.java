package coduck.igochaja.Model;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String socialId;
    private String email;
    private String password;
    private String nickName;
    private String social;
    private String report;
    private String image;

    @CreatedDate
    @Field("createdAt")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field("updatedAt")
    private LocalDateTime updatedAt;

    public User(String socialId, String nickName, String email, String social, String image) {

        this.socialId = socialId;
        this.nickName = nickName;
        this.email = email;
        this.social = social;
        this.image = image;
    }
}

