package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.Comment;
import src.redtalent.domain.Forum;

public interface ForumRepository extends MongoRepository<Forum, String> {

    Forum findForumByCommentsContaining(Comment comment);
}
