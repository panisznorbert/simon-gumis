package panisz.norbert.simongumis.views;

import com.vaadin.flow.router.Route;
import panisz.norbert.simongumis.components.SzolgaltatsokForm;
import javax.annotation.PostConstruct;

@Route("szolgaltatasok")
public class SzolgaltatsokView extends BaseView {
    @PostConstruct
    public void init() { this.initializeView();
    }

    private void initializeView() {
        fomenu.getSzolgaltatasok().getStyle().set("color", "blue");
        add(new SzolgaltatsokForm());
        setSizeFull();
    }
}
