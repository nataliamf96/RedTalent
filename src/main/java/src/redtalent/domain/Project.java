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
    private Integer maxParticipants;
    private String complexity;
    private Date startDate;
    private Date finishDate;
    private String attachedFiles;

    //Constructors -----------------------------------------------

    public Project(){
        super();
        this.tags = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.alerts = new ArrayList<>();
        this.projectMonitorings = new ArrayList<>();
    }

    public Project(String name, String image, String description, String requiredProfiles, Integer maxParticipants, String complexity, Date startDate, Date finishDate, String attachedFiles,
                   List<Tag> tags, List<Comment> comments, List<Alert> alerts, List<ProjectMonitoring> projectMonitorings, User userCreated){
        this.name = name;
        this.image = image;
        this.description = description;
        this.requiredProfiles = requiredProfiles;
        this.maxParticipants = maxParticipants;
        this.complexity = complexity;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.attachedFiles = attachedFiles;
        this.tags = tags;
        this.comments = comments;
        this.alerts = alerts;
        this.projectMonitorings = projectMonitorings;
        this.userCreated = userCreated;
    }

    //Getters and setters -----------------------------------------------

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

    @Size(min = 1)
    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    @NotNull
    @Future
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @NotNull
    @Future
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
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
    private User userCreated;

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

    public User getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(User userCreated) {
        this.userCreated = userCreated;
    }
}
