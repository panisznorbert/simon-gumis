package panisz.norbert.simongumis.view;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;


public class MenuView extends HorizontalLayout {
    AppLayout appLayout = new AppLayout();
    AppLayoutMenu menu = appLayout.createMenu();
    VerticalLayout tartalom = new VerticalLayout();

    public MenuView(){
        setMenuItem(menu, new AppLayoutMenuItem(VaadinIcon.EDIT.create(), "Gumik", e -> MainView.setTartalom("gumik")));
        setMenuItem(menu, new AppLayoutMenuItem(VaadinIcon.EDIT.create(), "Alap", e -> MainView.setTartalom("alap")));
        add(appLayout);
    }

    private void setMenuItem(AppLayoutMenu menu, AppLayoutMenuItem menuItem) {
        menuItem.getElement().setAttribute("theme", "icon-on-top");
        menu.addMenuItem(menuItem);
    }


}
