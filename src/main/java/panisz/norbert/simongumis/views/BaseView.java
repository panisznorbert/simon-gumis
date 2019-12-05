package panisz.norbert.simongumis.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import panisz.norbert.simongumis.components.FoMenu;
import panisz.norbert.simongumis.services.AdminService;
import panisz.norbert.simongumis.services.NyitvatartasService;
import panisz.norbert.simongumis.services.RendelesService;
import javax.annotation.PostConstruct;

@StyleSheet("fejlec.css")
public class BaseView extends VerticalLayout {

    @Autowired
    AdminService adminService;

    @Autowired
    RendelesService rendelesService;

    @Autowired
    NyitvatartasService nyitvatartasService;

    private HorizontalLayout fejlec = new HorizontalLayout();

    private Image balOldal = new Image("/images/baloldal.jpg", "");
    private Image jobbOldal = new Image("/images/jobboldal.jpg", "");

    FoMenu fomenu;

    @PostConstruct
    public void baseInit() { this.baseInitializeView();
    }

    private void baseInitializeView() {
        fomenu  = new FoMenu(adminService, nyitvatartasService);
        this.addClassName("alap");
        fejlec.addClassName("fejlec");
        fomenu.addClassName("menusor");
        balOldal.addClassName("fejlec-kepek");
        jobbOldal.addClassName("fejlec-kepek");
        if(rendelesService.sessionreKeres(UI.getCurrent().getSession().getSession().getId()) != null){
            fomenu.getKosar().setIcon(new Icon(VaadinIcon.CART));
            fomenu.getKosar().getStyle().set("color", "red");
        }
        fejlec.add(balOldal, fomenu, jobbOldal);
        add(fejlec);
        setSizeFull();
    }

}
