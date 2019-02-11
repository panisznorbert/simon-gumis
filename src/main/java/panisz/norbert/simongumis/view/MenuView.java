package panisz.norbert.simongumis.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import lombok.Data;
import panisz.norbert.simongumis.entities.RendelesEntity;
import panisz.norbert.simongumis.entities.RendelesiEgysegEntity;
import java.util.ArrayList;
import java.util.List;

@Data
public class MenuView extends HorizontalLayout {
    AppLayout appLayout = new AppLayout();
    AppLayoutMenu menu = appLayout.createMenu();

    private RendelesEntity aktualisRendelesek;

    public MenuView(){
        menuElemeinekBeallitasa(menu, new AppLayoutMenuItem(VaadinIcon.HOME.create(), "Kezdőlap", e -> MainView.setTartalom("alap")));
        menuElemeinekBeallitasa(menu, new AppLayoutMenuItem(VaadinIcon.TOOLS.create(), "Szolgáltatások", e -> MainView.setTartalom("alap")));
        menuElemeinekBeallitasa(menu, new AppLayoutMenuItem(VaadinIcon.COGS.create(), "Gumik kezelése", e -> MainView.setTartalom("gumik_kezelese")));
        menuElemeinekBeallitasa(menu, new AppLayoutMenuItem(VaadinIcon.BULLSEYE.create(), "Gumik", e -> MainView.setTartalom("gumik")));
        menuElemeinekBeallitasa(menu, new AppLayoutMenuItem(VaadinIcon.CALENDAR.create(), "Időpontfoglalás", e -> MainView.setTartalom("idopont_foglalas")));
        menuElemeinekBeallitasa(menu, new AppLayoutMenuItem(VaadinIcon.CART.create(), "Kosár", e -> MainView.setTartalom("kosar")));
        menuElemeinekBeallitasa(menu, new AppLayoutMenuItem(VaadinIcon.BOOK_DOLLAR.create(), "Rendelések", e -> MainView.setTartalom("rendelesek")));
        add(appLayout);

        aktualisRendelesekInit();
    }

    private void menuElemeinekBeallitasa(AppLayoutMenu menu, AppLayoutMenuItem menuItem) {
        menuItem.getElement().setAttribute("theme", "icon-on-top");
        menu.addMenuItem(menuItem);
    }

    public void aktualisRendelesekInit(){
        aktualisRendelesek = new RendelesEntity();
        List<RendelesiEgysegEntity> rendelesek = new ArrayList<>();
        aktualisRendelesek.setRendelesiEgysegek(rendelesek);
    }

}
