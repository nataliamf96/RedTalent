package src.redtalent.domain;

import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import src.redtalent.security.Role;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Document(collection = "Users")
public class User extends DomainEntity{

    //Attributes -----------------------------------------------

    @NotBlank
    private String fullname;
    @NotNull
    private boolean enabled;
    @DBRef
    private Set<Role> roles;
    private Set<Project> projects;
    private Set<Team> teams;
    private Set<Application> applications;
    private Set<Blog> blogs;
    private Set<Comment> comments;
    private Set<Reply> replies;
    private String image;
    private Account account;

    //Constructors -----------------------------------------------
    public User(){
        super();
    }

    public User(Account account,String fullname, Set<Project> projects, Set<Team> teams, Set<Application> applications, Set<Blog> blogs, Set<Comment> comments, Set<Reply> replies, String image){
        this.fullname = fullname;
        this.enabled = enabled;
        this.roles = roles;
        this.projects = projects;
        this.teams = teams;
        this.applications = applications;
        this.blogs = blogs;
        this.comments = comments;
        this.replies = replies;
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

    public Set<Blog> getBlogs() {
        return blogs;
    }

    public void setBlogs(Set<Blog> blogs) {
        this.blogs = blogs;
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

    public Set<Reply> getReplies() {
        return replies;
    }

    public void setReplies(Set<Reply> replies) {
        this.replies = replies;
    }
}
