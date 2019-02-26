package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@UIScope
@Component
public class UgyfelMezok extends HorizontalLayout {
    private TextField nev = new TextField("Név:");
    private TextField telefon = new TextField("Telefon:");
    private TextField email = new TextField("E-mail:");

    public UgyfelMezok(){
        nev.setRequired(true);
        telefon.setRequired(true);
        email.setRequired(true);
        add(nev, telefon, email);
    }

    public boolean kitoltottseg(){
        return nev.isInvalid() || telefon.isInvalid() || email.isInvalid() || nev.isEmpty() || telefon.isEmpty() || email.isEmpty();
    }

    public void alaphelyzet(){
        nev.clear();
        telefon.clear();
        email.clear();
    }
}
