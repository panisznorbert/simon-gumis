package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

@UIScope
@Component
public class LoginForm extends VerticalLayout {

    private FoMenu fomenu  = new FoMenu();

    private TextField felhasznalo = new TextField("Felhasználó:");

    private PasswordField jelszo = new PasswordField("Jelszó:");

    private Button belep = new Button("Belépés");

    private Label hiba = new Label();

    private HorizontalLayout belepesMezok = new HorizontalLayout(felhasznalo, jelszo,  belep );

    private VerticalLayout tartalom = new VerticalLayout(belepesMezok, hiba);

    public LoginForm(){
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
        add(fomenu, tartalom);
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
        hiba.setText("");
    }
}
