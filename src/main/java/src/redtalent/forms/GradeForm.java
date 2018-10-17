package src.redtalent.forms;

import org.bson.types.ObjectId;

import javax.validation.constraints.NotBlank;

public class GradeForm {

    private ObjectId gradeId;
    private String name;

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
}
