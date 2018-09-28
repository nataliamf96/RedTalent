package src.redtalent.domain;

import org.hibernate.validator.constraints.Range;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Evaluations")
public class Evaluation extends DomainEntity {

    // Attributes ---------------------------------------------------
    private Integer rate;
    private String description;

    // Constructors ---------------------------------------------------
    public Evaluation(){
        super();
    }

    public Evaluation(Integer rate, String description){
        this.rate = rate;
        this.description = description;
    }

    // Getters and setters ---------------------------------------------------

    @Range(min=1, max=5)
    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Relationships ---------------------------------------------------
}
