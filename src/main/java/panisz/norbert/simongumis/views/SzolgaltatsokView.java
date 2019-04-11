package panisz.norbert.simongumis.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import panisz.norbert.simongumis.components.SzolgaltatsokForm;
import javax.annotation.PostConstruct;

@Route("szolgaltatasok")
public class SzolgaltatsokView extends VerticalLayout {
    @PostConstruct
    public void init() { this.initializeView();
    }

    private void initializeView() {
        add(new SzolgaltatsokForm());
        setSizeFull();
    }
}
