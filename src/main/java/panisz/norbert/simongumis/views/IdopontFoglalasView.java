package panisz.norbert.simongumis.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import panisz.norbert.simongumis.components.IdopontFoglalasForm;

import javax.annotation.PostConstruct;

@Route("idopontfoglalas")
public class IdopontFoglalasView extends VerticalLayout {

    @Autowired
    private IdopontFoglalasForm idopontFoglalasForm;

    @PostConstruct
    public void init() { this.initializeView();
    }

    private void initializeView() {
        add(this.idopontFoglalasForm);
        setSizeFull();
    }
}
