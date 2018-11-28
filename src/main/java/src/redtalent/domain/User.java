package src.redtalent.domain;

import groovy.util.Eval;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import src.redtalent.security.Role;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

@Document(collection = "Users")
public class User extends DomainEntity{

    //Attributes -----------------------------------------------

    @NotBlank
    private String fullname;
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
    private Set<Forum> forums;
    private Set<Tag> tags;
    private Curriculum curriculum;
    private Set<Comment> commentsReceived;
    private List<Evaluation> evaluations;
    private List<Evaluation> evaluationsReceived;
    private List<Recomendation> recomendations;
    private List<Recomendation> recomendationsReceived;

    //Constructors -----------------------------------------------
    public User(){
        super();
    }

    public User(Curriculum curriculum,Set<Tag> tags,Account account, String fullname, List<Evaluation> evaluations, List<Evaluation> evaluationsReceived, List<Recomendation> recomendations, List<Recomendation> recomendationsReceived, Set<Project> projects, Set<Team> teams, Set<Application> applications, Set<Blog> blogs, Set<Forum> forums, Set<Comment> comments, Set<Comment> commentsReceived, Set<Reply> replies, String image){
        this.fullname = fullname;
        this.curriculum = curriculum;
        this.tags = tags;
        this.roles = roles;
        this.evaluations = evaluations;
        this.projects = projects;
        this.teams = teams;
        this.applications = applications;
        this.blogs = blogs;
        this.forums = forums;
        this.comments = comments;
        this.replies = replies;
        this.image = image;
        this.account = account;
        this.commentsReceived = commentsReceived;
        this.evaluationsReceived = evaluationsReceived;
        this.recomendations = recomendations;
        this.recomendationsReceived = recomendationsReceived;
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

    public Set<Forum> getForums() {
        return forums;
    }

    public void setForums(Set<Forum> forums) {
        this.forums = forums;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Curriculum getCurriculum() {
        return curriculum;
    }

    public void setCurriculum(Curriculum curriculum) {
        this.curriculum = curriculum;
    }

    public Set<Comment> getCommentsReceived() {
        return commentsReceived;
    }

    public void setCommentsReceived(Set<Comment> commentsReceived) {
        this.commentsReceived = commentsReceived;
    }

    public List<Evaluation> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(List<Evaluation> evaluations) {
        this.evaluations = evaluations;
    }

    public List<Evaluation> getEvaluationsReceived() {
        return evaluationsReceived;
    }

    public void setEvaluationsReceived(List<Evaluation> evaluationsReceived) {
        this.evaluationsReceived = evaluationsReceived;
    }

    public List<Recomendation> getRecomendations() {
        return recomendations;
    }

    public void setRecomendations(List<Recomendation> recomendations) {
        this.recomendations = recomendations;
    }

    public List<Recomendation> getRecomendationsReceived() {
        return recomendationsReceived;
    }

    public void setRecomendationsReceived(List<Recomendation> recomendationsReceived) {
        this.recomendationsReceived = recomendationsReceived;
    }
}
