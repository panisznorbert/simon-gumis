package panisz.norbert.simongumis.views;

import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import panisz.norbert.simongumis.components.KosarForm;
import panisz.norbert.simongumis.services.MegrendeltGumikService;

import javax.annotation.PostConstruct;

@Route("kosar")
public class KosarView extends BaseView {

    @PostConstruct
    public void init() { this.initializeView();
    }

    private void initializeView() {
        fomenu.getKosar().getStyle().set("color", "#75f3f9");
        tartalom.add(new KosarForm(rendelesService));
        setSizeFull();
    }

}
