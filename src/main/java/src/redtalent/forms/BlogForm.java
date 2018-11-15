package src.redtalent.forms;

import org.bson.types.ObjectId;
import src.redtalent.domain.Category;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class BlogForm {

    private String title;
    private String body;
    private String image;
    private Category category;
    private ObjectId subjectForumId;
    private ObjectId userId;

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

    @NotNull
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public ObjectId getSubjectForumId() {
        return subjectForumId;
    }

    public void setSubjectForumId(ObjectId subjectForumId) {
        this.subjectForumId = subjectForumId;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }
}
