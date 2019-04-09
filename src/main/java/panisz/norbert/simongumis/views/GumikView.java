package panisz.norbert.simongumis.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import panisz.norbert.simongumis.components.GumikForm;
import panisz.norbert.simongumis.services.GumiMeretekService;
import panisz.norbert.simongumis.services.GumikService;
import panisz.norbert.simongumis.services.RendelesService;
import javax.annotation.PostConstruct;

@Route("gumik")
public class GumikView extends VerticalLayout {
    @Autowired
    private GumikService gumikService;
    @Autowired
    private RendelesService rendelesService;
    @Autowired
    private GumiMeretekService gumiMeretekService;

    @PostConstruct
    public void init() { this.initializeView();
    }

    private void initializeView() {
        add(new GumikForm(gumikService, rendelesService, gumiMeretekService));
        setSizeFull();
    }
}
