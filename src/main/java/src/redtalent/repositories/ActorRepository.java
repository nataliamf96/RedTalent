package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.Actor;

import java.util.Collection;

public interface ActorRepository extends MongoRepository<Actor, String> {
    Boolean findByEmailExists(String email);
}