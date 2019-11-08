package panisz.norbert.simongumis.views;

import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import panisz.norbert.simongumis.components.KezdolapForm;
import panisz.norbert.simongumis.services.KezdolapTartalomService;

import javax.annotation.PostConstruct;

@Route("")
public class KezdolapView extends BaseView {
    @Autowired
    KezdolapTartalomService kezdolapTartalomService;

    @PostConstruct
    public void init() { this.initializeView();
    }

    private void initializeView() {
        fomenu.getKezdolap().getStyle().set("color", "#75f3f9");
        add(new KezdolapForm(kezdolapTartalomService));
        setSizeFull();
    }
}
