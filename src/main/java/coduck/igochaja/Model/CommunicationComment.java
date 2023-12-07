package coduck.igochaja.Model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Document(collection = "communication_comment") // 찾아주세요 댓글
public class CommunicationComment {
    @Id
    private String id;

    @DBRef
    private Communication communication;

    @DBRef
    private User user;

    private String content;
    private Date createdAt;
    private Date updatedAt;
    // getters, setters, constructors
}

