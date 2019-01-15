package panisz.norbert.simongumis.view;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class GumikView extends HorizontalLayout {

    TextField nev = new TextField("Név");
    TextField telefon = new TextField("Telefon");
    TextField email = new TextField("E-mail");

    public GumikView(){
        add(nev, telefon, email);

    }
}
