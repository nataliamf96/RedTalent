package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import src.redtalent.domain.Actor;

public interface ActorRepository extends MongoRepository<Actor, String> {

}
