package panisz.norbert.simongumis.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import panisz.norbert.simongumis.LoggerExample;
import panisz.norbert.simongumis.components.GumikForm;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

@Route("gumik")
public class GumikView extends VerticalLayout {
    private final static Logger LOGGER = Logger.getLogger(LoggerExample.class.getName());

    @Autowired
    private GumikForm gumikForm;

    @PostConstruct
    public void init() { this.initializeView();
    }

    private void initializeView() {
        LOGGER.info("GumikView-ba belépett");
        add(this.gumikForm);
        setSizeFull();
    }
}
