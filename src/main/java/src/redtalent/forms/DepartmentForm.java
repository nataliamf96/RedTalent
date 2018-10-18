package src.redtalent.forms;

import org.bson.types.ObjectId;
import src.redtalent.domain.Area;

import javax.validation.constraints.NotBlank;

public class DepartmentForm {

    private ObjectId departmentId;
    private String department;
    private Area area;

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

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }
}
