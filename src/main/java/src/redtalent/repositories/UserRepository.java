package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.User;

public interface UserRepository extends MongoRepository<User, String> {
}
