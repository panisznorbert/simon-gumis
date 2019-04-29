package panisz.norbert.simongumis.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import panisz.norbert.simongumis.LoggerExample;
import panisz.norbert.simongumis.components.FoMenu;
import panisz.norbert.simongumis.services.AdminService;
import panisz.norbert.simongumis.services.RendelesService;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

public class BaseView extends VerticalLayout {

    @Autowired
    AdminService adminService;

    @Autowired
    RendelesService rendelesService;

    FoMenu fomenu;

    private final static Logger LOGGER = Logger.getLogger(LoggerExample.class.getName());

    @PostConstruct
    public void baseInit() { this.baseInitializeView();
    }

    private void baseInitializeView() {
        fomenu  = new FoMenu(adminService);
        if(rendelesService.sessionreKeres(UI.getCurrent().getSession().getSession().getId()) != null){
            fomenu.getKosar().setIcon(new Icon(VaadinIcon.CART));
            fomenu.getKosar().getStyle().set("color", "red");
        }

        add(fomenu);
        setSizeFull();
    }

}
