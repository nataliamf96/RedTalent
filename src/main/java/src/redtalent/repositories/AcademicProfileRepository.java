package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.AcademicProfile;

public interface AcademicProfileRepository extends MongoRepository<AcademicProfile, String> {
}
