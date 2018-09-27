package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.Company;

public interface CompanyRepository extends MongoRepository<Company, String> {
}
