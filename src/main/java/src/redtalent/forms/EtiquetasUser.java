package src.redtalent.forms;

public class EtiquetasUser {
    private String tags;

    public EtiquetasUser(){
        super();
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "EtiquetasUser{" +
                "tags='" + tags + '\'' +
                '}';
    }
}
