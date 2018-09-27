package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.Tag;

public interface TagRepository extends MongoRepository<Tag, String> {
}
