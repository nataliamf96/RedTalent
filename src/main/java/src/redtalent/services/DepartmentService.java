package src.redtalent.services;

import com.mysema.commons.lang.Assert;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import src.redtalent.domain.Area;
import src.redtalent.domain.Department;
import src.redtalent.domain.Grade;
import src.redtalent.repositories.DepartmentRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public DepartmentService() {
        super();
    }

    public Department create(){
        Department department = new Department();
        return department;
    }

    public Department findOne(String departmentId) {
        Assert.notNull(departmentId, "Department Service : id null");
        Optional<Department> result = departmentRepository.findById(departmentId);
        return result.get();
    }

    public List<Department> findAll() {
        List<Department> result = departmentRepository.findAll();
        Assert.notNull(result, "Department Service : list null");
        return result;
    }

    public Department save(Department department) {
        Assert.notNull(department, "Department Service : Objeto null");
        Department result = departmentRepository.save(department);
        return result;
    }

    public void remove(Department department) {
        Assert.notNull(department, "Department Service : Objeto null");
        departmentRepository.delete(department);
    }

    public Department findDepartmentByGradesContaining(Grade grade){
        Assert.notNull(grade, "El grado no puede ser nulo");
        return departmentRepository.findDepartmentByGradesContaining(grade);
    }

    public List<Department> findAllByGradesContains(Grade grade){
        Assert.notNull(grade, "El grado no puede ser nulo");
        return departmentRepository.findAllByGradesContains(grade);
    }
}