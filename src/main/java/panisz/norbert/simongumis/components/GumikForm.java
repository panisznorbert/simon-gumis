package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;
import panisz.norbert.simongumis.entities.*;
import panisz.norbert.simongumis.services.AdminService;
import panisz.norbert.simongumis.services.GumiMeretekService;
import panisz.norbert.simongumis.services.GumikService;
import panisz.norbert.simongumis.services.RendelesService;
import java.util.ArrayList;
import java.util.List;

@StyleSheet("style.css")
@UIScope
@Component
public class GumikForm extends VerticalLayout {
    private GumiKeresoMenu menu;
    private GumikLap gumilap;

    public GumikForm(GumikService gumikService, RendelesService rendelesService, GumiMeretekService gumiMeretekService, FoMenu foMenu, AdminService adminService){

        menu = new GumiKeresoMenu(gumiMeretekService);
        gumilap = new GumikLap(gumikService.osszes(), gumikService, rendelesService, foMenu.getKosar(), adminService);
        add( menu, gumilap);
        this.setAlignItems(Alignment.CENTER);
        menu.getKeres().addClickListener(e -> {
            this.remove(gumilap);
            gumilap = new GumikLap(menu.szurtGumik(gumikService), gumikService, rendelesService, foMenu.getKosar(), adminService);
            add(gumilap);
        });
    }
}
