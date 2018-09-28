package src.redtalent.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "Phases")
public class Phase extends DomainEntity {

    // Attributes ---------------------------------------------------
    private String title;

    // Constructors ---------------------------------------------------

    public Phase(){
        super();
        this.teams = new ArrayList<>();
    }

    public Phase(String title, List<Team> teams){
        this.title = title;
        this.teams = teams;
    }

    // Getters and setters ---------------------------------------------------

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Relationships ---------------------------------------------------
    private List<Team> teams;

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }
}
