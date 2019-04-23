package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class LoginForm extends VerticalLayout {

    private MenuForm fomenu  = new MenuForm();

    public LoginForm(){
        add(fomenu);
    }
}
