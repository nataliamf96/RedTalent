package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.Subject;

public interface SubjectRepository extends MongoRepository<Subject, String> {
}
