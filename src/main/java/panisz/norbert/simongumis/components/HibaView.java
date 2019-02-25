package panisz.norbert.simongumis.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class HibaView extends Notification {

    public HibaView(String uzenet){
        Button kilep = new Button("Ok");
        Label leiras = new Label(uzenet);
        VerticalLayout tartalom = new VerticalLayout(leiras, kilep);
        add(tartalom);
        kilep.addClickListener(event -> close());
        this.setPosition(Notification.Position.MIDDLE);
        open();
    }
}
