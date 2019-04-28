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
    final String[] szolgaltatsok = {"Kerékszerelés", "Defekt javítás", "Kerék kiegyensúlyozás", "GUMIHOTEL", "Kellék anyagok értékesítése", "Gumiabroncs értékesítés (Új és Használt)"};
    final String[] kerekszerelesek = {"személy gépjármű", "kisteher gépjármű", "mezőgazdasági", "munkagép", "teher"};

    private FoMenu fomenu  = new FoMenu();

    private Label ujpont;

    private VerticalLayout tartalom = new VerticalLayout();

    public SzolgaltatsokForm(){
        fomenu.getSzolgaltatasok().getStyle().set("color", "blue");
        //this.setAlignItems(Alignment.CENTER);
        this.getStyle().set("padding-left","20%");
        tartalom.setWidth("400px");
        add(fomenu, tartalom);

        for(String szolgpont : szolgaltatsok){
            ujpont = new Label(szolgpont);
            tartalom.add(new HorizontalLayout(new Icon(VaadinIcon.ANGLE_RIGHT), new Label(szolgpont)));
            if("Kerékszerelés".equals(szolgpont)){
                for(String szerelespont : kerekszerelesek){
                    ujpont = new Label("- " + szerelespont);
                    ujpont.getStyle().set("padding-left", "50px");
                    tartalom.add(ujpont);

                }
            }
        }
    }
}
