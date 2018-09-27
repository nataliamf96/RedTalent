package src.redtalent.domain;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "Teams")
public class Team extends DomainEntity {

    //Attributes -----------------------------------------------
    private String name;
    private String description;
    private Date finishRegistration;
    private Integer participants;

    //Constructors -----------------------------------------------
    public Team(){
        super();
        this.applications = new ArrayList<>();
        this.evaluations = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.projects = new ArrayList<>();
    }

    public Team(String name, String description, Date finishRegistration, Integer participants, List<Application> applications, List<Evaluation> evaluations,
                User userCreated, List<Tag> tags, List<Comment> comments, List<Project> projects){
        this.name = name;
        this.description = description;
        this.finishRegistration = finishRegistration;
        this.participants = participants;
        this.applications = applications;
        this.evaluations = evaluations;
        this.userCreated = userCreated;
        this.tags = tags;
        this.comments = comments;
        this.projects = projects;
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
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    public Date getFinishRegistration() {
        return finishRegistration;
    }

    public void setFinishRegistration(Date finishRegistration) {
        this.finishRegistration = finishRegistration;
    }

    @NotBlank
    public Integer getParticipants() {
        return participants;
    }

    public void setParticipants(Integer participants) {
        this.participants = participants;
    }

    //Relationships -----------------------------------------------
    private List<Application> applications;
    private List<Evaluation> evaluations;
    private User userCreated;
    private List<Tag> tags;
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

    public User getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(User userCreated) {
        this.userCreated = userCreated;
    }

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

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
}
