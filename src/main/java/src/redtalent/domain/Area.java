package src.redtalent.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "Areas")
public class Area extends DomainEntity {

    private String area;

    public Area(){
        super();
        this.departaments = new ArrayList<Department>();
    }

    public Area(String area, List<Department> departments){
        this.area = area;
        this.departaments = departments;
    }

    public String getArea(){
        return area;
    }

    public void setArea(String area){
        this.area = area;
    }

    //Relationships

    private List<Department> departaments;

    public List<Department> getDepartaments(){
        return departaments;
    }

    public void setDepartaments(List<Department> departaments){
        this.departaments = departaments;
    }
}
