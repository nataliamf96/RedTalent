package src.redtalent.forms;

import org.bson.types.ObjectId;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.util.Date;

public class ProjectForm {

    private ObjectId id;
    private String name;
    private String image;
    private String description;
    private String requiredProfiles;
    private Integer maxParticipants;
    private String complexity;
    private String attachedFiles;
    private Boolean terms;
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

    @NotNull
    public Boolean getTerms() {
        return terms;
    }

    public void setTerms(Boolean terms) {
        this.terms = terms;
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
    @Pattern(regexp = "^HIGH|LOW|MEDIUM$")
    public String getComplexity() {
        return complexity;
    }

    public void setComplexity(String complexity) {
        this.complexity = complexity;
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

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
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

    @Override
    public String toString() {
        return "ProjectForm{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                ", requiredProfiles='" + requiredProfiles + '\'' +
                ", maxParticipants=" + maxParticipants +
                ", complexity='" + complexity + '\'' +
                ", attachedFiles='" + attachedFiles + '\'' +
                ", terms=" + terms +
                ", privado=" + privado +
                '}';
    }
}
