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

    @Indexed(unique = true, direction = IndexDirection.DESCENDING, dropDups = true)
    private String email;
    @Size(min = 5, max = 32)
    private String password;
    @NotBlank
    private String fullname;
    @NotNull
    private boolean enabled;
    @DBRef
    private Set<Role> roles;
    private Set<Project> projects;
    private Set<Team> teams;
    private Set<Application> applications;
    private Set<SubjectForum> subjectForums;
    private Set<Comment> comments;
    private String image;

    //Constructors -----------------------------------------------
    public User(){
        super();
    }

    public User(String email, String password, String fullname, Boolean enabled, Set<Role> roles, Set<Project> projects, Set<Team> teams, Set<Application> applications, Set<SubjectForum> subjectForums, Set<Comment> comments){
        this.email = email;
        this.password = password;
        this.fullname = fullname;
        this.enabled = enabled;
        this.roles = roles;
        this.projects = projects;
        this.teams = teams;
        this.applications = applications;
        this.subjectForums = subjectForums;
        this.comments = comments;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
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
}
