package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

@UIScope
@Component
public class SzolgaltatsokForm extends VerticalLayout {

    public SzolgaltatsokForm(){

        this.getStyle().set("padding-left","30%");
        VerticalLayout tartalom = new VerticalLayout();
        tartalom.setWidth("400px");
        add(tartalom);

        String[] szolgaltatsok = {"Kerékszerelés", "Defekt javítás", "Kerék kiegyensúlyozás", "GUMIHOTEL", "Kellék anyagok értékesítése", "Gumiabroncs értékesítés (Új és Használt)"};
        for(String szolgpont : szolgaltatsok){
            Label ujpont = new Label(szolgpont);
            ujpont.getStyle().set("font-weight", "bold");
            tartalom.add(new HorizontalLayout(new Icon(VaadinIcon.CHECK_SQUARE_O), ujpont));
            if("Kerékszerelés".equals(szolgpont)){
                String[] kerekszerelesek = {"személy gépjármű", "kisteher gépjármű", "mezőgazdasági", "munkagép", "teher"};
                for(String szerelespont : kerekszerelesek){
                    ujpont = new Label("- " + szerelespont);
                    ujpont.getStyle().set("padding-left", "50px");
                    tartalom.add(ujpont);

                }
            }
        }
    }
}
