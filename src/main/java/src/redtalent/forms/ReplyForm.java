package src.redtalent.forms;

import org.bson.types.ObjectId;

import javax.validation.constraints.NotBlank;

public class ReplyForm {

    private String title;
    private String text;
    private ObjectId blogId;
    private ObjectId commentId;
    private ObjectId userId;
    private ObjectId replyId;
    private ObjectId forumId;
    private ObjectId projectId;

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

    public ObjectId getReplyId() {
        return replyId;
    }

    public void setReplyId(ObjectId replyId) {
        this.replyId = replyId;
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
}
