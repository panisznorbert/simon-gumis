package panisz.norbert.simongumis.components;

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

    private MenuForm fomenu  = new MenuForm();

    private TextField felhasznalo = new TextField("Felhasználó:");

    private PasswordField jelszo = new PasswordField("Jelszó:");

    private Label hiba = new Label("1");

    private HorizontalLayout belepes = new HorizontalLayout(felhasznalo, jelszo);

    public LoginForm(){
        belepes.getStyle().set("margin-top","15%");
        belepes.getStyle().set("margin-top","10%");


        add(fomenu, new VerticalLayout(belepes, hiba));
    }
}
