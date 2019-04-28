package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

@UIScope
@Component
public class KezdolapForm extends VerticalLayout {
    private FoMenu fomenu  = new FoMenu();

    private Image szorolap = new Image("gumi1.jpg", "");

    public KezdolapForm(){
        fomenu.getKezdolap().getStyle().set("color", "blue");
        szorolap.setSizeFull();
        add(fomenu, szorolap);
    }
}
