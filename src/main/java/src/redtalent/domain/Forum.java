package src.redtalent.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document()
public class Forum extends DomainEntity{

    public Forum(){
        super();
        this.subjects = new ArrayList<Subject>();
    }

    public Forum(List<Subject> subjects){
        this.subjects = subjects;
    }

    private List<Subject> subjects;

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }
}
