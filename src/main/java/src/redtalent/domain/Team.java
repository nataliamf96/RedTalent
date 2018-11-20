package src.redtalent.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "Teams")
public class Team extends DomainEntity {

    //Attributes -----------------------------------------------
    private String name;
    private String description;
    private String image;
    private boolean closed;

    //Constructors -----------------------------------------------
    public Team(){
        super();
        this.applications = new ArrayList<>();
        this.evaluations = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.projects = new ArrayList<>();
    }

    public Team(String name, String description, boolean closed, List<Application> applications, List<Evaluation> evaluations, List<Comment> comments, List<Project> projects, String image){
        this.name = name;
        this.description = description;
        this.closed = closed;
        this.applications = applications;
        this.evaluations = evaluations;
        this.comments = comments;
        this.projects = projects;
        this.image = image;
    }

    //Getters and setters -----------------------------------------------

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

    @NotNull
    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    //Relationships -----------------------------------------------
    private List<Application> applications;
    private List<Evaluation> evaluations;
    private List<Comment> comments;
    private List<Project> projects;

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    public List<Evaluation> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(List<Evaluation> evaluations) {
        this.evaluations = evaluations;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
}