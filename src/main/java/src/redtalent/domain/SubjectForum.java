package src.redtalent.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "SubjectForums")
public class SubjectForum extends DomainEntity {

    private String title;
    private String body;
    private Date moment;

    public SubjectForum(){
        super();
        this.tags = new ArrayList<Tag>();
        this.comments = new ArrayList<Comment>();
    }

    public SubjectForum(String title, String body, Date moment, List<Tag> tags, List<Comment> comments){
        this.title = title;
        this.body = body;
        this.moment = moment;
        this.tags = tags;
        this.comments = comments;
    }

    @NotBlank
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NotBlank
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getMoment() {
        return moment;
    }

    public void setMoment(Date moment) {
        this.moment = moment;
    }

    //Relationships

    private List<Tag> tags;
    private List<Comment> comments;

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
}
