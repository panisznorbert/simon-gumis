package panisz.norbert.simongumis.views;

import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import panisz.norbert.simongumis.components.LefoglaltIdopontokForm;
import panisz.norbert.simongumis.services.IdopontFoglalasService;

import javax.annotation.PostConstruct;

@Route("lefoglalt_idopontok")
public class LefoglaltIdopontokView extends BaseView {
    @Autowired
    private IdopontFoglalasService foglalasService;

    @PostConstruct
    public void init() { this.initializeView();
    }

    private void initializeView() {
        fomenu.getLefoglaltIdopontok().getStyle().set("color", "blue");
        add(new LefoglaltIdopontokForm(foglalasService));
        setSizeFull();
    }
}
