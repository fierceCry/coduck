package coduck.igochaja.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "signup")
public class Signup {
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
}
