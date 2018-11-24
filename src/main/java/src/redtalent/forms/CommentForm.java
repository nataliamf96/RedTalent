package src.redtalent.forms;

import org.bson.types.ObjectId;

import javax.validation.constraints.NotBlank;

public class CommentForm {

    private String title;
    private String text;
    private ObjectId blogId;
    private ObjectId commentId;
    private ObjectId userId;
    private ObjectId forumId;
    private ObjectId projectId;
    private ObjectId teamId;

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

    public ObjectId getBlogId() {
        return blogId;
    }

    public void setBlogId(ObjectId blogId) {
        this.blogId = blogId;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public ObjectId getCommentId() {
        return commentId;
    }

    public void setCommentId(ObjectId commentId) {
        this.commentId = commentId;
    }

    public ObjectId getForumId() {
        return forumId;
    }

    public void setForumId(ObjectId forumId) {
        this.forumId = forumId;
    }

    public ObjectId getProjectId() {
        return projectId;
    }

    public void setProjectId(ObjectId projectId) {
        this.projectId = projectId;
    }

    public ObjectId getTeamId() {
        return teamId;
    }

    public void setTeamId(ObjectId teamId) {
        this.teamId = teamId;
    }
}
