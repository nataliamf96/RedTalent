package src.redtalent.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "ProjectMonitorings")
public class ProjectMonitoring extends DomainEntity {

    // Attributes ---------------------------------------------------
    private String title;
    private String description;
    private Date date;
    private String attachedFiles;

    // Constructors ---------------------------------------------------

    public ProjectMonitoring(){
        super();
    }

    public ProjectMonitoring(String title, String description, Date date, String attachedFiles){
        this.title = title;
        this.description = description;
        this.date = date;
        this.attachedFiles = attachedFiles;
    }

    // Getters and setters ---------------------------------------------------

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAttachedFiles() {
        return attachedFiles;
    }

    public void setAttachedFiles(String attachedFiles) {
        this.attachedFiles = attachedFiles;
    }

    // Relationships ---------------------------------------------------
}
