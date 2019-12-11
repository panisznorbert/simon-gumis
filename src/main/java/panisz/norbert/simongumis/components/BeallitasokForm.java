package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;
import panisz.norbert.simongumis.services.KezdolapTartalomService;
import panisz.norbert.simongumis.services.NyitvatartasService;

@StyleSheet("style.css")
@UIScope
@Component
public class BeallitasokForm extends VerticalLayout {

    public BeallitasokForm(NyitvatartasService nyitvatartasService, KezdolapTartalomService kezdolapTartalomService){
        VerticalLayout nyitvatartasModositasa = new NyitvatartasModositasa(nyitvatartasService);
        VerticalLayout kezdolapKepModositasa = new SzorolapModositasa(kezdolapTartalomService);
        kezdolapKepModositasa.addClassName("szegely");
        VerticalLayout kezdolapTartalmanakModositasa = new KezdolapTartalomKezelese(kezdolapTartalomService);
        kezdolapTartalmanakModositasa.addClassName("szegely");
        add(nyitvatartasModositasa, kezdolapKepModositasa, kezdolapTartalmanakModositasa);

    }

}
