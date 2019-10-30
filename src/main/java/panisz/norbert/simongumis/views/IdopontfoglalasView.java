package panisz.norbert.simongumis.views;

import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import panisz.norbert.simongumis.components.IdopontfoglalasForm;
import panisz.norbert.simongumis.services.IdopontfoglalasServie;
import panisz.norbert.simongumis.services.NyitvatartasService;

import javax.annotation.PostConstruct;

@Route("idopontfoglalas")
public class IdopontfoglalasView extends BaseView {

    @Autowired
        private IdopontfoglalasServie idopontfoglalasServie;

    @Autowired
    private NyitvatartasService nyitvatartasService;

    @PostConstruct
    public void init() { this.initializeView();
    }

    private void initializeView() {
        fomenu.getIdoponfoglalas().getStyle().set("color", "blue");
        add(new IdopontfoglalasForm(idopontfoglalasServie, nyitvatartasService));
        setSizeFull();
    }
}
