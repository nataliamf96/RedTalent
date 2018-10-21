package src.redtalent.forms;

import org.bson.types.ObjectId;
import src.redtalent.domain.Area;

import javax.validation.constraints.NotBlank;

public class DepartmentForm {

    private ObjectId departmentId;
    private String department;
    private ObjectId areaId;

    public ObjectId getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(ObjectId departmentId) {
        this.departmentId = departmentId;
    }

    @NotBlank
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public ObjectId getAreaId() {
        return areaId;
    }

    public void setAreaId(ObjectId areaId) {
        this.areaId = areaId;
    }
}
