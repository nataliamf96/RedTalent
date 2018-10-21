package src.redtalent.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "Teams")
public class Team extends DomainEntity {

    //Attributes -----------------------------------------------
    private String name;
    private String description;
    private boolean closed;

    //Constructors -----------------------------------------------
    public Team(){
        super();
        this.applications = new ArrayList<>();
        this.evaluations = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    public Team(String name, String description, boolean closed, List<Application> applications, List<Evaluation> evaluations,
                User userCreated, List<Comment> comments, Project project){
        this.name = name;
        this.description = description;
        this.closed = closed;
        this.applications = applications;
        this.evaluations = evaluations;
        this.userCreated = userCreated;
        this.comments = comments;
        this.project = project;
    }

    //Getters and setters -----------------------------------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    //Relationships -----------------------------------------------
    private List<Application> applications;
    private List<Evaluation> evaluations;
    private User userCreated;
    private List<Comment> comments;
    private Project project;

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

    public User getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(User userCreated) {
        this.userCreated = userCreated;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
