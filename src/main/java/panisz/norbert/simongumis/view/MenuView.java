package panisz.norbert.simongumis.view;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;


public class MenuView extends HorizontalLayout {
    AppLayout appLayout = new AppLayout();
    AppLayoutMenu menu = appLayout.createMenu();

    public MenuView(){
        setMenuItem(menu, new AppLayoutMenuItem(VaadinIcon.HOME.create(), "Kezdőlap", e -> MainView.setTartalom("alap")));
        setMenuItem(menu, new AppLayoutMenuItem(VaadinIcon.TOOLS.create(), "Szolgáltatások", e -> MainView.setTartalom("alap")));
        setMenuItem(menu, new AppLayoutMenuItem(VaadinIcon.COGS.create(), "Gumik kezelése", e -> MainView.setTartalom("gumik_kezelese")));
        setMenuItem(menu, new AppLayoutMenuItem(VaadinIcon.BULLSEYE.create(), "Gumik", e -> MainView.setTartalom("gumik")));
        setMenuItem(menu, new AppLayoutMenuItem(VaadinIcon.CALENDAR.create(), "Időpontfoglalás", e -> MainView.setTartalom("alap")));
        setMenuItem(menu, new AppLayoutMenuItem(VaadinIcon.CART.create(), "Kosár", e -> MainView.setTartalom("alap")));
        add(appLayout);
    }

    private void setMenuItem(AppLayoutMenu menu, AppLayoutMenuItem menuItem) {
        menuItem.getElement().setAttribute("theme", "icon-on-top");
        menu.addMenuItem(menuItem);
    }

}
