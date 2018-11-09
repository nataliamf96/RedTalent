package src.redtalent.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Document(collection = "Comments")
public class Comment extends DomainEntity {

    // Attributes ---------------------------------------------------
    private String title;
    private String text;
    private Date moment;

    // Constructors ---------------------------------------------------
    public Comment(){
        super();
    }

    public Comment(String title, String text, Date moment){
        this.title = title;
        this.text = text;
        this.moment = moment;
    }

    // Getters and setters ---------------------------------------------------

    @NotBlank
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NotBlank
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

}
