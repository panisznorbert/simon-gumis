package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;
import panisz.norbert.simongumis.services.AdminService;

@UIScope
@Component
public class KezdolapForm extends VerticalLayout {

    public KezdolapForm(AdminService adminService){


        //admin eszközök megjelenítése
        try {
            if (adminService.sessionreKeres(UI.getCurrent().getSession().getSession().getId()) != null) {
                // ide kell a főoldalon megjelenő kép módosításához egy gomb,
                // valamint egy másik gomba amivel szövegdobozokat lehet hozzáadni, a szövegdobozokhoz pedig törlési és szerkesztési lehetőség
                // ezen elemeket adatbázisban kell eltárolni: az elem megnevezése, az elem tartalma
            }
        }catch(Exception e){}
    }
}
