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
    private AppLayoutMenuItem gumikKezelese = new AppLayoutMenuItem(VaadinIcon.COGS.create(), "Gumik kezelése", e ->  UI.getCurrent().navigate("gumik_kezelese"));
    private AppLayoutMenuItem szolgaltatasok = new AppLayoutMenuItem(VaadinIcon.TOOLS.create(), "Szolgáltatások", e -> UI.getCurrent().navigate("szolgaltatasok"));
    private AppLayoutMenuItem kezdolap = new AppLayoutMenuItem(VaadinIcon.HOME.create(), "Kezdőlap", e -> UI.getCurrent().navigate(""));
    private AppLayoutMenuItem beallitasok = new AppLayoutMenuItem(VaadinIcon.COGS.create(), "Beállítások", e -> UI.getCurrent().navigate("beallitasok"));


    public FoMenu(AdminService adminService){
        AppLayoutMenu menu = appLayout.createMenu();


        menuElemeinekBeallitasa(menu, kezdolap);
        menuElemeinekBeallitasa(menu, szolgaltatasok);
        menuElemeinekBeallitasa(menu, gumik);
        menuElemeinekBeallitasa(menu, idoponfoglalas);
        menuElemeinekBeallitasa(menu, kosar);


        //bejelentkezéshez kötött menüpontok
        try {
            if (adminService.sessionreKeres(UI.getCurrent().getSession().getSession().getId()) != null) {
                menuElemeinekBeallitasa(menu, gumikKezelese);
                menuElemeinekBeallitasa(menu, lefoglaltIdopontok);
                menuElemeinekBeallitasa(menu, rendelesek);
                menuElemeinekBeallitasa(menu, beallitasok);
            }
        }catch(Exception e){}

        add(appLayout);

        this.setHeight("60px");
        this.appLayout.getElement().getStyle().set("height", "60px");
        this.appLayout.getElement().getStyle().set("padding", "0px");
        this.appLayout.getElement().getStyle().set("margin", "0px");
        this.getStyle().set("z-index", "99999");

    }

    private void menuElemeinekBeallitasa(AppLayoutMenu menu, AppLayoutMenuItem menuItem) {
        menuItem.getElement().setAttribute("theme", "icon-on-top");
        menu.addMenuItem(menuItem);
    }

}
