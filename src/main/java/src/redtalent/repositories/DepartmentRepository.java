package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.Department;
import src.redtalent.domain.Grade;


public interface DepartmentRepository extends MongoRepository<Department, String> {

    Department findDepartmentByGradesContaining(Grade grade);
    Department findDepartmentByGradesContains(Grade grade);
}