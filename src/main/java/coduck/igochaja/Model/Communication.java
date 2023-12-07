package coduck.igochaja.Model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Document(collection = "communication") // 찾아주세요
public class Communication {
    @Id
    private String id;
    @DBRef
    private User user;
    private String content;
    private int hits;
    private List<String> tags;
    private Date createdAt;
    private Date updatedAt;
    // getters, setters, constructors
}


