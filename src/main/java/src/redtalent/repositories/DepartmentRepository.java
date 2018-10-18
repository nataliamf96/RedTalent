package src.redtalent.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import src.redtalent.domain.Department;

import java.util.List;

public interface DepartmentRepository extends MongoRepository<Department, String> {


}