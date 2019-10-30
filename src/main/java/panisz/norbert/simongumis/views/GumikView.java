package panisz.norbert.simongumis.views;

import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import panisz.norbert.simongumis.components.GumikForm;
import panisz.norbert.simongumis.services.GumiMeretekService;
import panisz.norbert.simongumis.services.GumikService;

import javax.annotation.PostConstruct;

@Route("gumik")
public class GumikView extends BaseView {

    @Autowired
    private GumikService gumikService;

    @Autowired
    private GumiMeretekService gumiMeretekService;

    @PostConstruct
    public void init() { this.initializeView();
    }

    private void initializeView() {
        fomenu.getGumik().getStyle().set("color", "blue");
        add(new GumikForm(gumikService, rendelesService, gumiMeretekService, fomenu));
        setSizeFull();
    }

}
