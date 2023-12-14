package coduck.igochaja.Model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@ToString
@Document(collection = "communication_comment") // 찾아주세요 댓글
public class CommunicationComment {
    @Id
    private String id;

    @DBRef
    private Communication communication;

    @DBRef
    private User user;

    private String content;
    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;
    // getters, setters, constructors
}

