package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;
import panisz.norbert.simongumis.entities.NyitvatartasEntity;
import panisz.norbert.simongumis.services.AdminService;
import panisz.norbert.simongumis.services.NyitvatartasService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

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


    public FoMenu(AdminService adminService, NyitvatartasService nyitvatartasService){

        menuSzinek("black");

        AppLayoutMenu menu = appLayout.createMenu();

        menuElemeinekBeallitasa(menu, kezdolap);
        menuElemeinekBeallitasa(menu, szolgaltatasok);
        menuElemeinekBeallitasa(menu, gumik);
        menuElemeinekBeallitasa(menu, idoponfoglalas);
        menuElemeinekBeallitasa(menu, kosar);


        //bejelentkezéshez kötött menüpontok
        try {
            //if (adminService.sessionreKeres(UI.getCurrent().getSession().getSession().getId()) != null) {
                menuElemeinekBeallitasa(menu, gumikKezelese);
                menuElemeinekBeallitasa(menu, lefoglaltIdopontok);
                menuElemeinekBeallitasa(menu, rendelesek);
                menuElemeinekBeallitasa(menu, beallitasok);
            //}
        }catch(Exception e){}


        this.setAlignItems(Alignment.CENTER);

        add(appLayout, nyitvatartasMeghatarozo(nyitvatartasService));

        this.setHeight("60px");
        this.appLayout.getElement().getStyle().set("height", "60px");
        this.appLayout.getElement().getStyle().set("padding", "0px");
        this.appLayout.getElement().getStyle().set("margin", "0px");
        this.getStyle().set("z-index", "9");

    }

    private VerticalLayout nyitvatartasMeghatarozo(NyitvatartasService nyitvatartasService){
        VerticalLayout infosav = new VerticalLayout();
        HorizontalLayout nyitvatartas = new HorizontalLayout();
        Label nyitvatartasLabel = new Label();
        infosav.setAlignItems(Alignment.CENTER);
        infosav.setSizeFull();
        infosav.add(nyitvatartasLabel);
        infosav.getStyle().set("position", "fixed");
        infosav.getStyle().set("height", "60px");
        infosav.getStyle().set("background-color", "#f3f5f7");
        infosav.setWidth("100%");
        nyitvatartasLabel.getStyle().set("font-weight", "bold");
        NyitvatartasEntity elteroNyitvatartas = nyitvatartasService.adottNapNyitvatartasa(LocalDate.now());

        nyitvatartasLabel.getStyle().set("color", "red");

        if(elteroNyitvatartas != null){
            if(!elteroNyitvatartas.isNyitva()){
                nyitvatartasLabel.setText("Ma zárva van a műhely.");
                return infosav;
            }else{
                nyitvatartasLabel.setText("Ma nyitva: " + elteroNyitvatartas.getNyitas().toString() + " - " + elteroNyitvatartas.getZaras().toString());
            }
            if(nyitvavan(elteroNyitvatartas.getNyitas(), elteroNyitvatartas.getZaras())){
                nyitvatartasLabel.getStyle().set("color", "green");
            }
            return infosav;
        }
        switch(LocalDate.now().getDayOfWeek()){
            case SUNDAY:{
                nyitvatartasLabel.setText("Ma zárva van a műhely.");
                break;
            }
            case SATURDAY:{
                nyitvatartasLabel.setText("Ma nyitva: 7:00 - 12:00");
                if(nyitvavan(LocalTime.of(7, 0), LocalTime.of(12, 0))){
                    nyitvatartasLabel.getStyle().set("color", "green");
                }
                break;
            }
            default:{
                nyitvatartasLabel.setText("Ma nyitva: 7:00 - 17:00");
                if(nyitvavan(LocalTime.of(7, 0), LocalTime.of(17, 0))){
                    nyitvatartasLabel.getStyle().set("color", "green");
                }
                break;
            }
        }

        return infosav;
    }

    private void menuElemeinekBeallitasa(AppLayoutMenu menu, AppLayoutMenuItem menuItem) {
        menuItem.getElement().setAttribute("theme", "icon-on-top");
        menu.addMenuItem(menuItem);
    }

    private boolean nyitvavan(LocalTime nyitas, LocalTime zaras){
        if(LocalTime.now().isAfter(nyitas) && LocalTime.now().isBefore(zaras)){
            return true;
        }else return false;
    }

    private void menuSzinek(String szin){
        rendelesek.getStyle().set("color", szin);
        lefoglaltIdopontok.getStyle().set("color", szin);
        idoponfoglalas .getStyle().set("color", szin);
        kosar.getStyle().set("color", szin);
        gumik.getStyle().set("color", szin);
        gumikKezelese.getStyle().set("color", szin);
        szolgaltatasok.getStyle().set("color", szin);
        kezdolap.getStyle().set("color", szin);
        beallitasok.getStyle().set("color", szin);

    }

}
