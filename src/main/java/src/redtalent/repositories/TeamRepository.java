package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.Team;

public interface TeamRepository extends MongoRepository<Team, String> {
}
