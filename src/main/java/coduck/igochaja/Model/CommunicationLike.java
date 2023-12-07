package coduck.igochaja.Model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Document(collection = "communication_like") // 찾아주세요 좋아요
public class CommunicationLike {
    @Id
    private String id;

    @DBRef
    private Communication communication;

    @DBRef
    private User user;

    private Date createdAt;
    // getters, setters, constructors
}


