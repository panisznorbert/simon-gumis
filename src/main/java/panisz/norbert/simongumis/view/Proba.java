package panisz.norbert.simongumis.view;

import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import org.springframework.beans.factory.annotation.Autowired;
import panisz.norbert.simongumis.entities.UgyfelEntity;
import panisz.norbert.simongumis.repositories.UgyfelRepository;




public class Proba extends VerticalLayout implements KeyNotifier {
    private UgyfelEntity ugyfel;
    private UgyfelRepository ugyfelRepository;

    TextField nev = new TextField("Név");
    TextField telefon = new TextField("Telefon");
    TextField email = new TextField("E-mail");

    TextField db = new TextField("DB");

    Button save = new Button("Save", VaadinIcon.CHECK.create());
    Button szamol = new Button("Számol", VaadinIcon.CHECK.create());


    HorizontalLayout actions = new HorizontalLayout(save);
    Binder<UgyfelEntity> binder = new Binder<>(UgyfelEntity.class);
    private ChangeHandler changeHandler;

    @Autowired
    public Proba(UgyfelRepository ugyfelRepository){
        this.ugyfelRepository = ugyfelRepository;
        add(actions, nev, telefon, email, szamol, db);

        binder.bindInstanceFields(this);

        setSpacing(true);

        save.getElement().getThemeList().add("primary");

        save.addClickListener(e -> save());
        szamol.addClickListener(e -> db.setValue(new Integer(ugyfelRepository.findAll().size()).toString()));
        //setVisible(false);

    }

    void save() {
        ugyfelRepository.save(ugyfel);
        changeHandler.onChange();
    }

    public interface ChangeHandler {
        void onChange();
    }


    public void setChangeHandler(ChangeHandler h) {
        changeHandler = h;
    }
}
