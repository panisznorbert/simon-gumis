package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class MenuForm extends HorizontalLayout {
    private AppLayout appLayout = new AppLayout();

    public MenuForm(){
        AppLayoutMenu menu = appLayout.createMenu();
        menuElemeinekBeallitasa(menu, new AppLayoutMenuItem(VaadinIcon.HOME.create(), "Kezdőlap", e -> UI.getCurrent().navigate("/")));
        menuElemeinekBeallitasa(menu, new AppLayoutMenuItem(VaadinIcon.TOOLS.create(), "Szolgáltatások", e -> UI.getCurrent().navigate("szolgaltatasok")));
        menuElemeinekBeallitasa(menu, new AppLayoutMenuItem(VaadinIcon.COGS.create(), "Gumik kezelése", e ->  UI.getCurrent().navigate("gumikkezelese")));
        menuElemeinekBeallitasa(menu, new AppLayoutMenuItem(VaadinIcon.BULLSEYE.create(), "Gumik", e -> UI.getCurrent().navigate("gumik")));
        menuElemeinekBeallitasa(menu, new AppLayoutMenuItem(VaadinIcon.CALENDAR.create(), "Időpontfoglalás", e -> UI.getCurrent().navigate("idopontfoglalas")));
        menuElemeinekBeallitasa(menu, new AppLayoutMenuItem(VaadinIcon.CART.create(), "Kosár", e -> UI.getCurrent().navigate("kosar")));
        menuElemeinekBeallitasa(menu, new AppLayoutMenuItem(VaadinIcon.CALENDAR_USER.create(), "Lefoglalt időpontok", e -> UI.getCurrent().navigate("lefoglalt_idopontok")));
        menuElemeinekBeallitasa(menu, new AppLayoutMenuItem(VaadinIcon.BOOK_DOLLAR.create(), "Rendelések", e -> UI.getCurrent().navigate("rendelesek")));
        add(appLayout);
        
    }

    private void menuElemeinekBeallitasa(AppLayoutMenu menu, AppLayoutMenuItem menuItem) {
        menuItem.getElement().setAttribute("theme", "icon-on-top");
        menu.addMenuItem(menuItem);
    }

}
