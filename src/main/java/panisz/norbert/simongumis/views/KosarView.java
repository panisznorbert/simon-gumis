package panisz.norbert.simongumis.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import panisz.norbert.simongumis.components.KosarForm;

import javax.annotation.PostConstruct;

@Route("kosar")
public class KosarView extends VerticalLayout {

    @Autowired
    private KosarForm kosarForm;

    @PostConstruct
    public void init() { this.initializeView();
    }

    private void initializeView() {
        add(this.kosarForm);
        setSizeFull();
    }
}
