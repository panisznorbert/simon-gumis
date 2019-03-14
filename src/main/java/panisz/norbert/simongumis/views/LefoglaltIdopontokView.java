package panisz.norbert.simongumis.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import panisz.norbert.simongumis.components.LefoglaltIdopontokForm;

import javax.annotation.PostConstruct;

@Route("lefoglalt_idopontok")
public class LefoglaltIdopontokView extends VerticalLayout {
    @Autowired
    private LefoglaltIdopontokForm lefoglaltIdopontokForm;

    @PostConstruct
    public void init() { this.initializeView();
    }

    private void initializeView() {
        add(this.lefoglaltIdopontokForm);
        setSizeFull();
    }
}
