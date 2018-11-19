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
    private Boolean privado;
    private Boolean estado;
    private Boolean cerrado;

    //Constructors -----------------------------------------------

    public Project(){
        super();
        this.comments = new ArrayList<>();
        this.alerts = new ArrayList<>();
        this.forums = new ArrayList<>();
    }

    public Project(String name, String image, String description, String requiredProfiles,
                   List<Comment> comments, List<Alert> alerts, User userCreated, Boolean privado, Boolean estado, Category categorie,
                   List<Forum> forums,Boolean cerrado){
        this.name = name;
        this.cerrado = cerrado;
        this.image = image;
        this.description = description;
        this.requiredProfiles = requiredProfiles;
        this.comments = comments;
        this.alerts = alerts;
        this.privado = privado;
        this.estado = estado;
        this.categorie = categorie;
        this.forums = forums;
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

    //Relationships -----------------------------------------------

    private List<Comment> comments;
    private List<Alert> alerts;
    private Category categorie;
    private List<Forum> forums;

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

    public List<Forum> getForums() {
        return forums;
    }

    public void setForums(List<Forum> forums) {
        this.forums = forums;
    }

    public Boolean getCerrado() {
        return cerrado;
    }

    public void setCerrado(Boolean cerrado) {
        this.cerrado = cerrado;
    }
}
