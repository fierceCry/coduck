package coduck.igochaja.Service;

import coduck.igochaja.Model.Comment;

public interface CommentService {
    Comment createComment(Comment comment);
    Comment updateComment(String id, Comment comment);
    void deleteComment(String id);
    Comment getCommentById(String id);
}
