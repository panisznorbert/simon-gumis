package panisz.norbert.simongumis.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import panisz.norbert.simongumis.components.GumikKezeleseForm;

import javax.annotation.PostConstruct;

@Route("gumikkezelese")
public class GumikKezeleseView extends VerticalLayout {

    @Autowired
    private GumikKezeleseForm gumikKezeleseForm;

    @PostConstruct
    public void init() { this.initializeView();
    }

    private void initializeView() {
        add(this.gumikKezeleseForm);
        setSizeFull();
    }
}
