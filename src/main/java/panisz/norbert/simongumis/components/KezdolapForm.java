package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;
import panisz.norbert.simongumis.views.BaseView;

@UIScope
@Component
public class KezdolapForm extends VerticalLayout {

    private Image szorolap = new Image("gumi1.jpg", "");

    public KezdolapForm(){

        szorolap.setSizeFull();
        add(szorolap);
    }
}
