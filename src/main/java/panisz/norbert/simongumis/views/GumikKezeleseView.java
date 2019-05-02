package panisz.norbert.simongumis.views;

import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import panisz.norbert.simongumis.components.GumikKezeleseForm;
import panisz.norbert.simongumis.services.GumikService;

import javax.annotation.PostConstruct;

@Route("gumik_kezelese")
public class GumikKezeleseView extends BaseView {

    @Autowired
    private GumikService gumikService;


    @PostConstruct
    public void init() { this.initializeView();
    }

    private void initializeView() {
        fomenu.getGumikKezelese().getStyle().set("color", "blue");
        add(new GumikKezeleseForm(gumikService));
        setSizeFull();
    }
}
