package src.redtalent.forms;

import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;

public class CurriculumForm {

    private String urlLinkedin;
    private String area;
    private String escuela;
    private String grado;

    public CurriculumForm(){
        super();
    }

    @URL
    @NotBlank
    public String getUrlLinkedin() {
        return urlLinkedin;
    }

    public void setUrlLinkedin(String urlLinkedin) {
        this.urlLinkedin = urlLinkedin;
    }

    @NotBlank
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @NotBlank
    public String getEscuela() {
        return escuela;
    }

    public void setEscuela(String escuela) {
        this.escuela = escuela;
    }

    @NotBlank
    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }
}
