package coduck.igochaja.Model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
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
    private Date createdAt;
    private Date updatedAt;
    // getter, setter, constructors
}
