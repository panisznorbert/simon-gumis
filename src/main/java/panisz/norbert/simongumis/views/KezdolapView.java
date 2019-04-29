package panisz.norbert.simongumis.views;

import com.vaadin.flow.router.Route;
import panisz.norbert.simongumis.components.KezdolapForm;

import javax.annotation.PostConstruct;

@Route("")
public class KezdolapView extends BaseView {

    @PostConstruct
    public void init() { this.initializeView();
    }

    private void initializeView() {
        fomenu.getKezdolap().getStyle().set("color", "blue");
        add(new KezdolapForm());
        setSizeFull();
    }
}
