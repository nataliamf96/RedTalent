package src.redtalent.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Comment extends DomainEntity {

    // Attributes ---------------------------------------------------
    private String title;
    private String text;
    private Date moment;

    // Constructors ---------------------------------------------------
    public Comment(){
        super();
        comments = new ArrayList<>();
    }

    public Comment(String title, String text, Date moment, List<Comment> comments){
        this.title = title;
        this.text = text;
        this.moment = moment;
        this.comments = comments;
    }

    // Getters and setters ---------------------------------------------------

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    public Date getMoment() {
        return moment;
    }

    public void setMoment(Date moment) {
        this.moment = moment;
    }

    // Relationships ---------------------------------------------------
    private List<Comment> comments;

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
