package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class HibaJelzes  extends Notification {

    public HibaJelzes(String uzenet){
        Button kilep = new Button("Ok");
        Label leiras = new Label(uzenet);
        VerticalLayout tartalom = new VerticalLayout(leiras, kilep);
        add(tartalom);
        kilep.addClickListener(event -> close());
        this.setPosition(Notification.Position.MIDDLE);
        this.setDuration(6000);
    }
}