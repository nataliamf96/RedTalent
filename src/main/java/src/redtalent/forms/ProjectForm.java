package src.redtalent.forms;

import org.bson.types.ObjectId;
import org.springframework.format.annotation.DateTimeFormat;
import src.redtalent.domain.Category;

import javax.validation.constraints.*;
import java.util.Date;

public class ProjectForm {

    private ObjectId id;
    private String name;
    private String image;
    private String description;
    private String requiredProfiles;
    private String attachedFiles;
    private String category;
    private Boolean privado;

    public ProjectForm(){
        super();
    }

    //Getters and setters -----------------------------------------------


    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

   @NotBlank
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @NotBlank
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotBlank
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NotBlank
    public String getRequiredProfiles() {
        return requiredProfiles;
    }

    public void setRequiredProfiles(String requiredProfiles) {
        this.requiredProfiles = requiredProfiles;
    }

    public String getAttachedFiles() {
        return attachedFiles;
    }

    public void setAttachedFiles(String attachedFiles) {
        this.attachedFiles = attachedFiles;
    }

    public Boolean getPrivado() {
        return privado;
    }

    public void setPrivado(Boolean privado) {
        this.privado = privado;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "ProjectForm{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                ", requiredProfiles='" + requiredProfiles + '\'' +
                ", attachedFiles='" + attachedFiles + '\'' +
                ", category=" + category +
                ", privado=" + privado +
                '}';
    }
}
