package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.Administrator;

public interface AdministratorRepository extends MongoRepository<Administrator, String> {
    Administrator findByEmail(String email);
}
