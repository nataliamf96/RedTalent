package src.redtalent.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import src.redtalent.domain.Department;
import src.redtalent.domain.Grade;

import java.util.List;


public interface DepartmentRepository extends MongoRepository<Department, String> {

    Department findDepartmentByGradesContaining(Grade grade);
    List<Department> findAllByGradesContains(Grade grade);
}