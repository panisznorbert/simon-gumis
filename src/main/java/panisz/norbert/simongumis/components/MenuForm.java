package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.Data;
import lombok.EqualsAndHashCode;
import panisz.norbert.simongumis.LoggerExample;
import panisz.norbert.simongumis.spring.SpringApplication;

import java.util.logging.Logger;

@EqualsAndHashCode(callSuper = true)
@Data
class MenuForm extends VerticalLayout {
    private AppLayout appLayout = new AppLayout();
    private AppLayoutMenuItem rendelesek = new AppLayoutMenuItem(VaadinIcon.BOOK_DOLLAR.create(), "Rendelések", e -> UI.getCurrent().navigate("rendelesek"));
    private AppLayoutMenuItem lefoglaltIdopontok = new AppLayoutMenuItem(VaadinIcon.CALENDAR_USER.create(), "Lefoglalt időpontok", e -> UI.getCurrent().navigate("lefoglalt_idopontok"));
    private AppLayoutMenuItem idoponfoglalas = new AppLayoutMenuItem(VaadinIcon.CALENDAR.create(), "Időpontfoglalás", e -> UI.getCurrent().navigate("idopontfoglalas"));
    private AppLayoutMenuItem kosar = new AppLayoutMenuItem(VaadinIcon.CART_O.create(), "Kosár", e -> UI.getCurrent().navigate("kosar"));
    private AppLayoutMenuItem gumik = new AppLayoutMenuItem(VaadinIcon.BULLSEYE.create(), "Gumik", e -> UI.getCurrent().navigate("gumik"));
    private AppLayoutMenuItem gumikKezelese = new AppLayoutMenuItem(VaadinIcon.COGS.create(), "Gumik kezelése", e ->  UI.getCurrent().navigate("gumikkezelese"));
    private AppLayoutMenuItem szolgaltatasok = new AppLayoutMenuItem(VaadinIcon.TOOLS.create(), "Szolgáltatások", e -> UI.getCurrent().navigate("szolgaltatasok"));
    private AppLayoutMenuItem kezdolap = new AppLayoutMenuItem(VaadinIcon.HOME.create(), "Kezdőlap", e -> UI.getCurrent().navigate(""));

    private final static Logger LOGGER = Logger.getLogger(LoggerExample.class.getName());

    MenuForm(){
        AppLayoutMenu menu = appLayout.createMenu();

        if(SpringApplication.getRendelesAzon()!=null){
            this.kosar.getStyle().set("color", "red");
            this.kosar.setIcon(new Icon(VaadinIcon.CART));
        }

        menuElemeinekBeallitasa(menu, kezdolap);
        menuElemeinekBeallitasa(menu, szolgaltatasok);
        menuElemeinekBeallitasa(menu, gumikKezelese);
        menuElemeinekBeallitasa(menu, gumik);
        menuElemeinekBeallitasa(menu, idoponfoglalas);
        menuElemeinekBeallitasa(menu, kosar);
        menuElemeinekBeallitasa(menu, lefoglaltIdopontok);
        menuElemeinekBeallitasa(menu, rendelesek);
        add(appLayout);

        this.setHeight("60px");
        this.appLayout.getElement().getStyle().set("height", "60px");
        this.getStyle().set("z-index", "99999");

    }

    private void menuElemeinekBeallitasa(AppLayoutMenu menu, AppLayoutMenuItem menuItem) {
        menuItem.getElement().setAttribute("theme", "icon-on-top");
        menu.addMenuItem(menuItem);
    }

}
