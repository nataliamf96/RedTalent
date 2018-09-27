package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.Grade;

public interface GradeRepository extends MongoRepository<Grade, String> {
}
