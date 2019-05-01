package panisz.norbert.simongumis.views;

import com.vaadin.flow.router.Route;
import panisz.norbert.simongumis.components.RendelesekForm;

import javax.annotation.PostConstruct;

@Route("rendelesek")
public class RendelesekView extends BaseView {

    @PostConstruct
    public void init() { this.initializeView();
    }

    private void initializeView() {
        fomenu.getRendelesek().getStyle().set("color", "blue");
        add(new RendelesekForm(rendelesService));
        setSizeFull();
    }
}
