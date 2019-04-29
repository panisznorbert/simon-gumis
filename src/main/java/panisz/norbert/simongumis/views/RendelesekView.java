package panisz.norbert.simongumis.views;

import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import panisz.norbert.simongumis.components.RendelesekForm;
import panisz.norbert.simongumis.services.RendelesService;

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
