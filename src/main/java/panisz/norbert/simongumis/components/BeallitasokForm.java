package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;
import panisz.norbert.simongumis.services.NyitvatartasService;


@UIScope
@Component
public class BeallitasokForm extends VerticalLayout {

    private VerticalLayout nyitvatartasModositasa;

    private VerticalLayout kezdolapKepModositasa;

    public BeallitasokForm(NyitvatartasService nyitvatartasService){
        nyitvatartasModositasa = new NyitvatartasModositasa(nyitvatartasService);
        kezdolapKepModositasa = new KezdoKepModositasa();
        add(nyitvatartasModositasa, kezdolapKepModositasa);

    }


}
