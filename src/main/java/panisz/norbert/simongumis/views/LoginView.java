package panisz.norbert.simongumis.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import panisz.norbert.simongumis.components.LoginForm;
import javax.annotation.PostConstruct;

@Route("admin")
public class LoginView extends VerticalLayout {
    @PostConstruct
    public void init() { this.initializeView();
    }

    private void initializeView() {
        add(new LoginForm());
        setSizeFull();
    }
}
