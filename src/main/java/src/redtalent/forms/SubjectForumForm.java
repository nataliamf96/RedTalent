package src.redtalent.forms;

import org.bson.types.ObjectId;

import javax.validation.constraints.NotBlank;

public class SubjectForumForm {

    private String title;
    private String body;
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
