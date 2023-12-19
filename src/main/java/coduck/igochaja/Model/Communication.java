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
import java.util.List;

@Getter
@Setter
@ToString
@Document(collection = "communication")
public class Communication {
    @Id
    private String id;
    @DBRef
    private User user;
    private String content;
    private int hits;
    private List<String> tags;
    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;
}


