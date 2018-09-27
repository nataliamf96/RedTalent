package src.redtalent.security;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import src.redtalent.domain.Actor;

public interface UserAccountRepository extends MongoRepository<UserAccount, String> {
    UserAccount findByUsername(String username);

    @Query("{ 'id' : ?0 }")
    Actor findByUserAccountId(int userAccountId);
}
