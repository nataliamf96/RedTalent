package src.redtalent.forms;

import org.bson.types.ObjectId;

import javax.validation.constraints.NotBlank;

public class AreaForm {

    private ObjectId areaId;
    private String area;

    public ObjectId getAreaId() {
        return areaId;
    }

    public void setAreaId(ObjectId areaId) {
        this.areaId = areaId;
    }

    @NotBlank
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
