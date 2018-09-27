package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.Evaluation;

public interface EvaluationRepository extends MongoRepository<Evaluation, String> {
}
