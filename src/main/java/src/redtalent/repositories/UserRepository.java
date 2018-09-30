package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import src.redtalent.domain.User;

public interface UserRepository extends MongoRepository<User, String> {

    User findByUserAccountId(String id);
}
