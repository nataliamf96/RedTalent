package src.redtalent.domain;

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
