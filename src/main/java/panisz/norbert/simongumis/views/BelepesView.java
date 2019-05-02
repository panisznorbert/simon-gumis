package panisz.norbert.simongumis.views;

import com.vaadin.flow.router.Route;
import panisz.norbert.simongumis.components.BelepesForm;

import javax.annotation.PostConstruct;

@Route("belepes")
public class BelepesView extends BaseView {

    @PostConstruct
    public void init() { this.initializeView();
    }

    private void initializeView() {
        add(new BelepesForm(adminService));
        setSizeFull();
    }
}
