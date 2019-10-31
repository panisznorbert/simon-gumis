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
        fomenu.getKezdolap().getStyle().set("color", "#75f3f9");
        add(new KezdolapForm());
        setSizeFull();
    }
}
