package panisz.norbert.simongumis.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import panisz.norbert.simongumis.components.KezdolapForm;

import javax.annotation.PostConstruct;

@Route("")
public class KezdolapView extends VerticalLayout {
    @PostConstruct
    public void init() { this.initializeView();
    }

    private void initializeView() {
        add(new KezdolapForm());
        setSizeFull();
    }
}
