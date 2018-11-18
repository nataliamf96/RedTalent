package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.Category;
import src.redtalent.domain.Comment;
import src.redtalent.domain.Forum;

import java.util.List;

public interface ForumRepository extends MongoRepository<Forum, String> {

    Forum findForumByCommentsContaining(Comment comment);
    List<Forum> findAllByCategory(Category category);
}
