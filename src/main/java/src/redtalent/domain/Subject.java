package src.redtalent.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "Subjects")
public class Subject extends DomainEntity {

    private String title;
    private String body;
    private String image;
    private Date moment;

    public Subject(){
        super();
        this.comments = new ArrayList<Comment>();
    }

    public Subject(String title, String body, Date moment, String image, Category category, List<Comment> comments){
        this.title = title;
        this.body = body;
        this.moment = moment;
        this.image = image;
        this.category = category;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getMoment() {
        return moment;
    }

    public void setMoment(Date moment) {
        this.moment = moment;
    }

    //Relationships

    private Category category;

    private List<Comment> comments;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
