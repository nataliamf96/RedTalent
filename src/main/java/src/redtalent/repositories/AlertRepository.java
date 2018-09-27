package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.Alert;

public interface AlertRepository extends MongoRepository<Alert, String> {
}
