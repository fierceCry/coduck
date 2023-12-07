package coduck.igochaja.Model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Document(collection = "communication_comment_like") // 찾아주세요 댓글 좋아요
public class CommunicationCommentLike {
    @Id
    private String id;

    @DBRef
    private CommunicationComment communicationComment;

    @DBRef
    private User user;

    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;
    // getters, setters, constructors
}
