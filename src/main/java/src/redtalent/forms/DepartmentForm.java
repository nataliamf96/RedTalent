package src.redtalent.forms;

import org.bson.types.ObjectId;

import javax.validation.constraints.NotBlank;

public class DepartmentForm {

    private ObjectId departmentId;
    private String department;

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
}
