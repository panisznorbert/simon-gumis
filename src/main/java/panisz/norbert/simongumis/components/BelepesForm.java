package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;
import panisz.norbert.simongumis.entities.AdminEntity;
import panisz.norbert.simongumis.services.AdminService;

@UIScope
@Component
public class BelepesForm extends VerticalLayout {

    private AdminService adminService;

    private TextField felhasznalo = new TextField("Felhasználó:");

    private PasswordField jelszo = new PasswordField("Jelszó:");

    private Button belep = new Button("Belépés");

    private Label hiba = new Label();

    private HorizontalLayout belepesMezok = new HorizontalLayout(felhasznalo, jelszo,  belep );

    private VerticalLayout tartalom = new VerticalLayout(belepesMezok, hiba);

    public BelepesForm(AdminService adminService){
        this.adminService = adminService;
        hiba.setText("");
        hiba.getStyle().set("color","red");
        felhasznalo.setRequired(true);
        felhasznalo.setValue("");
        jelszo.setRequired(true);
        jelszo.setValue("");
        belepesMezok.getStyle().set("margin-top","50px");
        belepesMezok.setAlignItems(Alignment.BASELINE);
        hiba.getStyle().set("margin-top","30px");
        tartalom.setAlignItems(Alignment.CENTER);
        belep.addClickListener(e -> belepes());
        add(tartalom);
    }

    private void belepes(){
        if(felhasznalo.isEmpty()){
            hiba.setText("A felhasználó kitöltése kötelező!");
            return;
        }
        if(jelszo.isEmpty()){
            hiba.setText("A jelszó kitöltése kötelező!");
            return;
        }

        AdminEntity adminEntity = adminService.adminraKeres(felhasznalo.getValue(), jelszo.getValue());

        if(adminEntity == null){
            hiba.setText("Hibás felhasználó név vagy jelszó!");
            return;
        }

        adminEntity.setSession(UI.getCurrent().getSession().getSession().getId());
        try {
            adminService.ment(adminEntity);
        }catch(Exception ex){
            hiba.setText("Hiba a belépésnél, kérem próbálja újra.");
        }
        UI.getCurrent().navigate("");
        hiba.setText("");
    }
}
