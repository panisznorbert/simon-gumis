package panisz.norbert.simongumis.views;

import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import panisz.norbert.simongumis.components.FoMenu;
import panisz.norbert.simongumis.components.IdopontFoglalasForm;
import panisz.norbert.simongumis.services.IdopontFoglalasService;

import javax.annotation.PostConstruct;

@Route("idopontfoglalas")
public class IdopontFoglalasView extends BaseView {

    private FoMenu fomenu  = new FoMenu(adminService);

    @Autowired
    private IdopontFoglalasService idopontFoglalasService;

    @PostConstruct
    public void init() { this.initializeView();
    }

    private void initializeView() {
        fomenu.getIdoponfoglalas().getStyle().set("color", "blue");
        add(new IdopontFoglalasForm(idopontFoglalasService));
        setSizeFull();
    }
}
