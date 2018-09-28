package src.redtalent.domain;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Alerts")
public class Alert extends DomainEntity {

    // Attributes ---------------------------------------------------
    private String text;

    // Constructors ---------------------------------------------------

    public Alert(){
        super();
    }

    public Alert(String text){
        this.text = text;
    }

    // Getters and setters ---------------------------------------------------

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    // Relationships ---------------------------------------------------


}
