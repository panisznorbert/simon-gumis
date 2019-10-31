package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

@UIScope
@Component
public class KezdolapForm extends VerticalLayout {

    VerticalLayout alap = new VerticalLayout();

    public KezdolapForm(){


        Image szorolap = new Image("images/borito.png", "");

        szorolap.setWidth("60%");

        alap.setAlignItems(Alignment.CENTER);

        alap.add(szorolap);

        this.add(alap);

    }
}
