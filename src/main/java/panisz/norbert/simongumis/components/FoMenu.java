package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;
import panisz.norbert.simongumis.services.AdminService;

@UIScope
@Component
@EqualsAndHashCode(callSuper = true)
@Data
public class FoMenu extends VerticalLayout {

    private AppLayout appLayout = new AppLayout();
    private AppLayoutMenuItem rendelesek = new AppLayoutMenuItem(VaadinIcon.BOOK_DOLLAR.create(), "Rendelések", e -> UI.getCurrent().navigate("rendelesek"));
    private AppLayoutMenuItem lefoglaltIdopontok = new AppLayoutMenuItem(VaadinIcon.CALENDAR_USER.create(), "Lefoglalt időpontok", e -> UI.getCurrent().navigate("lefoglalt_idopontok"));
    private AppLayoutMenuItem idoponfoglalas = new AppLayoutMenuItem(VaadinIcon.CALENDAR.create(), "Időpontfoglalás", e -> UI.getCurrent().navigate("idopontfoglalas"));
    private AppLayoutMenuItem kosar = new AppLayoutMenuItem(VaadinIcon.CART_O.create(), "Kosár", e -> UI.getCurrent().navigate("kosar"));
    private AppLayoutMenuItem gumik = new AppLayoutMenuItem(VaadinIcon.BULLSEYE.create(), "Gumik", e -> UI.getCurrent().navigate("gumik"));
    private AppLayoutMenuItem szolgaltatasok = new AppLayoutMenuItem(VaadinIcon.TOOLS.create(), "Szolgáltatások", e -> UI.getCurrent().navigate("szolgaltatasok"));
    private AppLayoutMenuItem kezdolap = new AppLayoutMenuItem(VaadinIcon.HOME.create(), "Kezdőlap", e -> UI.getCurrent().navigate(""));
    private AppLayoutMenuItem beallitasok = new AppLayoutMenuItem(VaadinIcon.COGS.create(), "Beállítások", e -> UI.getCurrent().navigate("beallitasok"));


    public FoMenu(AdminService adminService){

        menuSzinek("white");
        AppLayoutMenu menu = appLayout.createMenu();
        appLayout.removeBranding();
        menuElemeinekBeallitasa(menu, kezdolap);
        menuElemeinekBeallitasa(menu, szolgaltatasok);
        menuElemeinekBeallitasa(menu, gumik);
        menuElemeinekBeallitasa(menu, idoponfoglalas);
        menuElemeinekBeallitasa(menu, kosar);


        //bejelentkezéshez kötött menüpontok
        try {
            if (adminService.sessionreKeres(UI.getCurrent().getSession().getSession().getId()) != null) {
                menuElemeinekBeallitasa(menu, lefoglaltIdopontok);
                menuElemeinekBeallitasa(menu, rendelesek);
                menuElemeinekBeallitasa(menu, beallitasok);
            }
        }catch(Exception e){}

        add(appLayout);
    }

    private void menuElemeinekBeallitasa(AppLayoutMenu menu, AppLayoutMenuItem menuItem) {
        menuItem.getElement().setAttribute("theme", "icon-on-top");
        menu.addMenuItem(menuItem);
    }

    private void menuSzinek(String szin){
        rendelesek.getStyle().set("color", szin);
        lefoglaltIdopontok.getStyle().set("color", szin);
        idoponfoglalas .getStyle().set("color", szin);
        kosar.getStyle().set("color", szin);
        gumik.getStyle().set("color", szin);
        szolgaltatasok.getStyle().set("color", szin);
        kezdolap.getStyle().set("color", szin);
        beallitasok.getStyle().set("color", szin);

    }

}
