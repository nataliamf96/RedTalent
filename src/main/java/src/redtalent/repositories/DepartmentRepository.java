package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.Department;

public interface DepartmentRepository extends MongoRepository<Department, String> {

}