package src.redtalent.forms;

import org.bson.types.ObjectId;

import javax.validation.constraints.NotBlank;

public class CommentForm {

    private String title;
    private String text;
    private ObjectId subjectForumId;
    private ObjectId commentId;
    private ObjectId userId;

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
