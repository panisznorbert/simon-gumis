package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;
import panisz.norbert.simongumis.services.AdminService;
import panisz.norbert.simongumis.services.GumiMeretekService;
import panisz.norbert.simongumis.services.GumikService;
import panisz.norbert.simongumis.services.RendelesService;

@StyleSheet("almenu.css")
@UIScope
@Component
public class GumikForm extends VerticalLayout {
    private GumiKeresoMenu menu;
    private GumikLap gumilap;


    public GumikForm(GumikService gumikService, RendelesService rendelesService, GumiMeretekService gumiMeretekService, FoMenu foMenu, AdminService adminService){
        this.setId("alap");
        Button keres = new Button("Keresés", new Icon(VaadinIcon.SEARCH));
        Button uj = new Button("Új gumi");
        HorizontalLayout menusor = new HorizontalLayout(keres);
        //if (adminService.sessionreKeres(UI.getCurrent().getSession().getSession().getId()) != null){
            menusor.add(uj);
        //}
        menusor.setId("menusor");
        menusor.setSizeUndefined();
        VerticalLayout almenu = new VerticalLayout(menusor);
        almenu.setId("almenu");
        almenu.setSizeUndefined();

        menu = new GumiKeresoMenu(gumiMeretekService);
        gumilap = new GumikLap(gumikService.osszes(), gumikService, rendelesService, foMenu.getKosar(), adminService);
        gumilap.setId("gumilap");
        add(almenu, gumilap);
        this.setAlignItems(Alignment.CENTER);
        menu.getKeres().addClickListener(e -> {
            this.remove(gumilap);
            gumilap = new GumikLap(menu.szurtGumik(gumikService), gumikService, rendelesService, foMenu.getKosar(), adminService);
            add(gumilap);
        });
    }
}
