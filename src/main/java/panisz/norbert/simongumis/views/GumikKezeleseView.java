package panisz.norbert.simongumis.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import panisz.norbert.simongumis.components.GumikKezeleseForm;
import panisz.norbert.simongumis.services.GumikService;

import javax.annotation.PostConstruct;

@Route("gumikkezelese")
public class GumikKezeleseView extends VerticalLayout {

    @Autowired
    private GumikService gumikService;

    @PostConstruct
    public void init() { this.initializeView();
    }

    private void initializeView() {
        add(new GumikKezeleseForm(gumikService));
        setSizeFull();
    }
}
