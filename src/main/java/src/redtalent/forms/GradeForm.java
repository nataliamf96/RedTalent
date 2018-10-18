package src.redtalent.forms;

import org.bson.types.ObjectId;
import src.redtalent.domain.Department;
import src.redtalent.domain.Grade;

import javax.validation.constraints.NotBlank;

public class GradeForm {

    private ObjectId gradeId;
    private String name;
    private Department department;

    public ObjectId getGradeId() {
        return gradeId;
    }

    public void setGradeId(ObjectId gradeId) {
        this.gradeId = gradeId;
    }

    @NotBlank
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
