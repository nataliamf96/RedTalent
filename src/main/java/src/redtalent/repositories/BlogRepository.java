package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.Blog;
import src.redtalent.domain.Comment;

public interface BlogRepository extends MongoRepository<Blog, String> {

    Blog findBlogByCommentsContaining(Comment comment);
}
