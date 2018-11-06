package src.redtalent.domain;

import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import src.redtalent.security.Role;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Document(collection = "Users")
public class User extends DomainEntity{

    //Attributes -----------------------------------------------

    @NotBlank
    private String fullname;
    private Set<Project> projects;
    private Set<Team> teams;
    private Set<Application> applications;
    private Set<SubjectForum> subjectForums;
    private Set<Comment> comments;
    private String image;
    private Account account;

    //Constructors -----------------------------------------------
    public User(){
        super();
    }

    public User(Account account,String fullname, Set<Project> projects, Set<Team> teams, Set<Application> applications, Set<SubjectForum> subjectForums, Set<Comment> comments, String image){
        this.fullname = fullname;
        this.projects = projects;
        this.teams = teams;
        this.applications = applications;
        this.subjectForums = subjectForums;
        this.comments = comments;
        this.image = image;
        this.account = account;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Set<Application> getApplications() {
        return applications;
    }

    public void setApplications(Set<Application> applications) {
        this.applications = applications;
    }

    public Set<SubjectForum> getSubjectForums() {
        return subjectForums;
    }

    public void setSubjectForums(Set<SubjectForum> subjectForums) {
        this.subjectForums = subjectForums;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

}
