package panisz.norbert.simongumis.views;

import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import panisz.norbert.simongumis.components.FoMenu;
import panisz.norbert.simongumis.components.IdopontfoglalasForm;
import panisz.norbert.simongumis.services.IdopontfoglalasServie;

import javax.annotation.PostConstruct;

@Route("idopontfoglalas")
public class IdopontfoglalasView extends BaseView {

    private FoMenu fomenu  = new FoMenu(adminService);

    @Autowired
    private IdopontfoglalasServie idopontfoglalasServie;

    @PostConstruct
    public void init() { this.initializeView();
    }

    private void initializeView() {
        fomenu.getIdoponfoglalas().getStyle().set("color", "blue");
        add(new IdopontfoglalasForm(idopontfoglalasServie));
        setSizeFull();
    }
}
