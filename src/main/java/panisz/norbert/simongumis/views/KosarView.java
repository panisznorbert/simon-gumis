package panisz.norbert.simongumis.views;

import com.vaadin.flow.router.Route;
import panisz.norbert.simongumis.components.KosarForm;
import javax.annotation.PostConstruct;

@Route("kosar")
public class KosarView extends BaseView {

    @PostConstruct
    public void init() { this.initializeView();
    }

    private void initializeView() {
        fomenu.getKosar().getStyle().set("color", "blue");
        add(new KosarForm(rendelesService));
        setSizeFull();
    }

}
