package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.Comment;
import src.redtalent.domain.Reply;

public interface CommentRepository extends MongoRepository<Comment, String> {

    Comment findCommentByRepliesContaining(Reply reply);
}
