package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.Phase;

public interface PhaseRepository extends MongoRepository<Phase, String> {
}
