package src.redtalent.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "Projects")
public class Project extends DomainEntity{

    //Attributes -----------------------------------------------

    private String name;
    private String image;
    private String description;
    private String requiredProfiles;
    private String attachedFiles;
    private Boolean privado;
    private Boolean estado;

    //Constructors -----------------------------------------------

    public Project(){
        super();
        this.tags = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.alerts = new ArrayList<>();
        this.projectMonitorings = new ArrayList<>();
    }

    public Project(String name, String image, String description, String requiredProfiles, String attachedFiles,
                   List<Tag> tags, List<Comment> comments, List<Alert> alerts, List<ProjectMonitoring> projectMonitorings, User userCreated, Boolean privado, Boolean estado, Category categorie,
                   Forum forum){
        this.name = name;
        this.image = image;
        this.description = description;
        this.requiredProfiles = requiredProfiles;
        this.attachedFiles = attachedFiles;
        this.tags = tags;
        this.comments = comments;
        this.alerts = alerts;
        this.projectMonitorings = projectMonitorings;
        this.privado = privado;
        this.estado = estado;
        this.categorie = categorie;
        this.forum = forum;
    }

    //Getters and setters -----------------------------------------------
    public Boolean getPrivado() {
        return privado;
    }

    public void setPrivado(Boolean privado) {
        this.privado = privado;
    }

    @javax.validation.constraints.NotBlank
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

    //Relationships -----------------------------------------------

    private List<Tag> tags;
    private List<Comment> comments;
    private List<Alert> alerts;
    private List<ProjectMonitoring> projectMonitorings;
    private Category categorie;
    private Forum forum;

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Alert> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<Alert> alerts) {
        this.alerts = alerts;
    }

    public List<ProjectMonitoring> getProjectMonitorings() {
        return projectMonitorings;
    }

    public void setProjectMonitorings(List<ProjectMonitoring> projectMonitorings) {
        this.projectMonitorings = projectMonitorings;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Category getCategorie() {
        return categorie;
    }

    public void setCategorie(Category categorie) {
        this.categorie = categorie;
    }

    public Forum getForum() { return forum; }

    public void setForum(Forum forum) {this.forum = forum; }
}
