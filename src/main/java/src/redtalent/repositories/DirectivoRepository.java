package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.Account;
import src.redtalent.domain.Directivo;

public interface DirectivoRepository extends MongoRepository<Directivo, String> {

    Directivo findByAccount(Account account);

}
