package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.Application;

public interface ApplicationRepository extends MongoRepository<Application, String> {
}
