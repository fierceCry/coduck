package coduck.igochaja.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import coduck.igochaja.Model.Comment;
import coduck.igochaja.Repository.CommentRepository;
import java.util.Date;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment createComment(Comment comment) {
        comment.setCreatedAt(new Date());
        comment.setUpdatedAt(new Date());
        return commentRepository.save(comment);
    }

    @Override
    public Comment updateComment(String id, Comment comment) {
        // Update logic based on your requirements
        Comment existingComment = commentRepository.findById(id).orElse(null);
        if (existingComment != null) {
            existingComment.setContent(comment.getContent());
            existingComment.setUpdatedAt(new Date());
            return commentRepository.save(existingComment);
        }
        return null;
    }

    @Override
    public void deleteComment(String id) {
        commentRepository.deleteById(id);
    }

    @Override
    public Comment getCommentById(String id) {
        return commentRepository.findById(id).orElse(null);
    }
}
