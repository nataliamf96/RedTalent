package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.Blog;
import src.redtalent.domain.Category;
import src.redtalent.domain.Comment;

import java.util.List;
import java.util.Set;

public interface BlogRepository extends MongoRepository<Blog, String> {

    Blog findBlogByCommentsContaining(Comment comment);
    List<Blog> findAllByCategory(Category category);
}
