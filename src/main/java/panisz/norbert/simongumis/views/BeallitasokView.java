package panisz.norbert.simongumis.views;

import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import panisz.norbert.simongumis.components.BeallitasokForm;
import panisz.norbert.simongumis.services.IdopontfoglalasServie;

import javax.annotation.PostConstruct;

@Route("beallitasok")
public class BeallitasokView extends BaseView {

    @Autowired
    private IdopontfoglalasServie idopontfoglalasServie;

    @PostConstruct
    public void init() { this.initializeView();
    }

    private void initializeView() {
        add(new BeallitasokForm());
        setSizeFull();
    }
}
