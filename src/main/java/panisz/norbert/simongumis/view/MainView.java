package panisz.norbert.simongumis.view;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import panisz.norbert.simongumis.entities.UgyfelEntity;
import panisz.norbert.simongumis.repositories.UgyfelRepository;




@Route
public class MainView extends VerticalLayout implements KeyNotifier {
    private UgyfelEntity ugyfel;
    TextField nev = new TextField("Név");
    TextField tel = new TextField("Telefon");
    TextField email = new TextField("E-mail");
    Button ment = new Button("Mentés", VaadinIcon.CHECK.create());

    Binder<UgyfelEntity> binder = new Binder<>(UgyfelEntity.class);

    @Autowired
    public MainView(UgyfelRepository ugyfelRepository) {
        add(nev, tel, email, ment);

        binder.bindInstanceFields(this);

        setSpacing(true);

        addKeyPressListener(Key.ENTER, e -> ugyfelRepository.save(ugyfel));



    }
}
