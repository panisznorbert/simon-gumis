package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;
import panisz.norbert.simongumis.entities.GumikEntity;
import panisz.norbert.simongumis.services.AdminService;
import panisz.norbert.simongumis.services.GumiMeretekService;
import panisz.norbert.simongumis.services.GumikService;
import panisz.norbert.simongumis.services.RendelesService;

import java.util.List;

@StyleSheet("almenu.css")
@UIScope
@Component
public class GumikForm extends VerticalLayout {
    private GumikLap gumilap;
    private VerticalLayout almenu = new VerticalLayout();

    private GumikService gumikService;
    private RendelesService rendelesService;
    private GumiMeretekService gumiMeretekService;
    private AdminService adminService;
    private FoMenu foMenu;

    public GumikForm(GumikService gumikService, RendelesService rendelesService, GumiMeretekService gumiMeretekService, FoMenu foMenu, AdminService adminService){
        this.gumikService = gumikService;
        this.rendelesService = rendelesService;
        this.gumiMeretekService = gumiMeretekService;
        this.foMenu = foMenu;
        this.adminService = adminService;
        this.setId("alap");
        gumilap = new GumikLap(gumikService.osszes(), gumikService, rendelesService, foMenu.getKosar(), adminService);
        almenu.setSizeUndefined();
        initMenusor();

        add(almenu, gumilap);
        this.setAlignItems(Alignment.CENTER);

    }

    private void initMenusor(){

        gumilap.setId("gumilap");
        almenu.removeAll();
        almenu.setId("almenu");
        HorizontalLayout menusor = new HorizontalLayout();
        Button keres = new Button("Keresés", new Icon(VaadinIcon.SEARCH));
        keres.addClickListener(e -> Kereses());
        ComboBox<String> rendezes = new ComboBox<>("", "Gyártó", "Ár/növekvő", "Ár/csökkenő", "Méret");
        rendezes.setPlaceholder("Sorrend");
        rendezes.setId("rendezes");
        menusor.add(keres, rendezes);

        //if (adminService.sessionreKeres(UI.getCurrent().getSession().getSession().getId()) != null){
        Button uj = new Button("Új gumi");
        uj.addClickListener(e -> UjElem());
        menusor.add(uj);
        //}
        menusor.setId("menusor");
        menusor.setSizeUndefined();

        almenu.add(menusor);
    }

    private void Kereses(){
        Icon kilep = new Icon(VaadinIcon.ARROW_BACKWARD);
        kilep.setId("kilep");
        kilep.addClickListener(e -> initMenusor());
        gumilap.setId("gumilap-kereses");
        almenu.removeAll();
        almenu.setId("almenu-kereses");
        GumiKeresoMenu menu = new GumiKeresoMenu(gumiMeretekService);
        menu.setId("menu");
        menu.getKeres().addClickListener(e -> {
            List<GumikEntity> szurtGumik = menu.szurtGumik(gumikService);
            if(szurtGumik != null){
                this.remove(gumilap);
                gumilap = new GumikLap(szurtGumik, gumikService, rendelesService, foMenu.getKosar(), adminService);
                add(gumilap);
                initMenusor();
            }

        });

        almenu.add(kilep, menu);
    }

    private void UjElem(){
        Icon kilep = new Icon(VaadinIcon.ARROW_BACKWARD);
        kilep.setId("kilep");
        kilep.addClickListener(e -> initMenusor());
        gumilap.setId("gumilap-uj-elem");
        almenu.removeAll();
        almenu.setId("almenu-uj-elem");
        GumikLapSor elemFelvetele = new GumikLapSor(gumikService, rendelesService, foMenu.getKosar(), adminService);

        almenu.add(kilep, elemFelvetele);
    }
}
