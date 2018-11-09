package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.AcademicProfile;
import src.redtalent.domain.Account;

public interface AccountRepository extends MongoRepository<Account, String> {
    Account findByEmail(String email);
}
