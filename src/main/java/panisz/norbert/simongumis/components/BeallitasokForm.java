package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;
import panisz.norbert.simongumis.services.IdopontfoglalasServie;


@UIScope
@Component
public class BeallitasokForm extends VerticalLayout {

    private VerticalLayout nyitvatartasModositasa;

    public BeallitasokForm(){
        nyitvatartasModositasa = new NyitvatartasModositasa();
        add(nyitvatartasModositasa);

    }


}
