package panisz.norbert.simongumis.views;

import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import panisz.norbert.simongumis.components.BeallitasokForm;
import panisz.norbert.simongumis.services.NyitvatartasService;

import javax.annotation.PostConstruct;

@Route("beallitasok")
public class BeallitasokView extends BaseView {

    @Autowired
    private NyitvatartasService nyitvatartasService;

    @PostConstruct
    public void init() { this.initializeView();
    }

    private void initializeView() {
        fomenu.getBeallitasok().getStyle().set("color", "#75f3f9");
        add(new BeallitasokForm(nyitvatartasService));
        setSizeFull();
    }
}
