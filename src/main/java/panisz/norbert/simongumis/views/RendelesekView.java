package panisz.norbert.simongumis.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import panisz.norbert.simongumis.components.RendelesekForm;
import panisz.norbert.simongumis.services.RendelesService;

import javax.annotation.PostConstruct;

@Route("rendelesek")
public class RendelesekView extends VerticalLayout {

    @Autowired
    private RendelesService rendelesService;

    @PostConstruct
    public void init() { this.initializeView();
    }

    private void initializeView() {
        add(new RendelesekForm(rendelesService));
        setSizeFull();
    }
}
