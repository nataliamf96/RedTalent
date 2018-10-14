package src.redtalent.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "Departments")
public class Department extends DomainEntity {

    private String department;

    public Department(){
        super();
        this.grades = new ArrayList<Grade>();
    }

    public Department(String department, List<Grade> grades){
        this.department = department;
        this.grades = grades;
    }

    @NotBlank
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    //Relationships -------

    private List<Grade> grades;

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }
}
