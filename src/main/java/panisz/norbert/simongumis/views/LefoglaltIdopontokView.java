package panisz.norbert.simongumis.views;

import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import panisz.norbert.simongumis.components.LefoglaltIdopontokForm;
import panisz.norbert.simongumis.services.IdopontfoglalasServie;

import javax.annotation.PostConstruct;

@Route("lefoglalt_idopontok")
public class LefoglaltIdopontokView extends BaseView {
    @Autowired
    private IdopontfoglalasServie foglalasService;

    @PostConstruct
    public void init() { this.initializeView();
    }

    private void initializeView() {
        fomenu.getLefoglaltIdopontok().getStyle().set("color", "#75f3f9");
        tartalom.add(new LefoglaltIdopontokForm(foglalasService));
        setSizeFull();
    }
}
