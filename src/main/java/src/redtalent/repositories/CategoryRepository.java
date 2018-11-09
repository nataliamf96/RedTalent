package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.Category;
import src.redtalent.domain.Tag;

public interface CategoryRepository extends MongoRepository<Category, String> {
}
