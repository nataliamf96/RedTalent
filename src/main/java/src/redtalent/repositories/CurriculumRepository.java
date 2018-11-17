package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.AcademicProfile;
import src.redtalent.domain.Curriculum;

public interface CurriculumRepository extends MongoRepository<Curriculum, String> {
}
