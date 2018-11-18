package src.redtalent.forms;

import org.bson.types.ObjectId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AlertForm {
    private String text;
    private ObjectId project;

    public AlertForm(){
        super();
    }

    @NotBlank
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @NotNull
    public ObjectId getProject() {
        return project;
    }

    public void setProject(ObjectId project) {
        this.project = project;
    }
}
