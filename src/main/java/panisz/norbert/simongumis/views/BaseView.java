package panisz.norbert.simongumis.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import org.springframework.beans.factory.annotation.Autowired;
import panisz.norbert.simongumis.components.FoMenu;
import panisz.norbert.simongumis.entities.NyitvatartasEntity;
import panisz.norbert.simongumis.services.AdminService;
import panisz.norbert.simongumis.services.NyitvatartasService;
import panisz.norbert.simongumis.services.RendelesService;
import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalTime;

@StyleSheet("fejlec.css")
public class BaseView extends SplitLayout {

    @Autowired
    AdminService adminService;

    @Autowired
    RendelesService rendelesService;

    @Autowired
    NyitvatartasService nyitvatartasService;

    private HorizontalLayout fejlec = new HorizontalLayout();

    private Image jobbOldal = new Image("/images/fejlec.jpg", "");
    VerticalLayout tartalom = new VerticalLayout();
    private VerticalLayout lablec = new VerticalLayout();

    FoMenu fomenu;

    @PostConstruct
    public void baseInit() { this.baseInitializeView();
    }

    private void baseInitializeView() {
        fomenu  = new FoMenu(adminService);
        lablec.setHeight("100px");
        this.addClassName("alap");
        tartalom.addClassName("tartalom");
        fejlec.addClassName("fejlec");
        fomenu.addClassName("menusor");
        jobbOldal.addClassName("jobb-fejlec-kep");
        if(rendelesService.sessionreKeres(UI.getCurrent().getSession().getSession().getId()) != null){
            fomenu.getKosar().setIcon(new Icon(VaadinIcon.CART));
            fomenu.getKosar().getStyle().set("color", "red");
        }

        fejlec.add(nyitvatartasMeghatarozo(nyitvatartasService), fomenu, jobbOldal);
        this.addToPrimary(fejlec);
        this.addToSecondary(tartalom);

        setSizeFull();
    }

    private VerticalLayout nyitvatartasMeghatarozo(NyitvatartasService nyitvatartasService){
        VerticalLayout infosav = new VerticalLayout();
        infosav.setSizeUndefined();
        Label nyitvatartasLabel = new Label();
        nyitvatartasLabel.addClassName("nyitvatartas");
        infosav.addClassName("infosav");
        infosav.add(nyitvatartasLabel);
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

    private boolean nyitvavan(LocalTime nyitas, LocalTime zaras){
        return LocalTime.now().isAfter(nyitas) && LocalTime.now().isBefore(zaras);
    }

}
