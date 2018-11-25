package src.redtalent.forms;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

public class EvaluationForm {

    private Integer rate;
    private ObjectId userId;
    private ObjectId projectId;
    private ObjectId evaluationId;

    @Range(min=1, max=5)
    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public ObjectId getProjectId() {
        return projectId;
    }

    public void setProjectId(ObjectId projectId) {
        this.projectId = projectId;
    }

    public ObjectId getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(ObjectId evaluationId) {
        this.evaluationId = evaluationId;
    }
}
