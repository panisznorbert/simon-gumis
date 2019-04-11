package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

@UIScope
@Component
public class KezdolapForm extends VerticalLayout {
    private MenuForm fomenu  = new MenuForm();

    public KezdolapForm(){
        fomenu.getKezdolap().getStyle().set("color", "blue");
        add(fomenu);
    }
}
