package src.redtalent.forms;

import org.bson.types.ObjectId;
import src.redtalent.domain.Department;
import src.redtalent.domain.Grade;

import javax.validation.constraints.NotBlank;

public class GradeForm {

    private ObjectId gradeId;
    private String name;
    private ObjectId departmentId;
    private ObjectId areaId;

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

    public ObjectId getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(ObjectId departmentId) {
        this.departmentId = departmentId;
    }

    public ObjectId getAreaId() {
        return areaId;
    }

    public void setAreaId(ObjectId areaId) {
        this.areaId = areaId;
    }
}
