package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.Blog;

public interface BlogRepository extends MongoRepository<Blog, String> {
}
