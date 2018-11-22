package src.redtalent.forms;

import javax.validation.constraints.NotNull;

public class FiltradoForm {

    private String text;

    public FiltradoForm(){
        super();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "FiltradoForm{" +
                "text='" + text + '\'' +
                '}';
    }
}
