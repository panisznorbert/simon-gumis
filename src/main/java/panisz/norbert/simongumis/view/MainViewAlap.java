package panisz.norbert.simongumis.view;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import org.springframework.beans.factory.annotation.Autowired;
import panisz.norbert.simongumis.entities.UgyfelEntity;
import panisz.norbert.simongumis.repositories.UgyfelRepository;

import java.util.ArrayList;
import java.util.Collection;



public class MainViewAlap extends VerticalLayout implements KeyNotifier {

    TextField nev = new TextField("Név");
    TextField telefon = new TextField("Telefon");
    TextField email = new TextField("E-mail");
    Button ment = new Button("Mentés", VaadinIcon.CHECK.create());
    Button betolt = new Button("Betölt", VaadinIcon.CHECK.create());
    Button szamol = new Button("Számol", VaadinIcon.CHECK.create());

    TextField db = new TextField("DB");
    TextField nev2 = new TextField("Név");
    TextField telefon2 = new TextField("Telefon");
    TextField email2 = new TextField("E-mail");



    @Autowired
    public MainViewAlap(UgyfelRepository ugyfelRepository) {
        add(nev, telefon, email, ment, betolt, db, szamol);

        addKeyPressListener(Key.ENTER, e -> ment(ugyfelRepository));
        ment.addClickListener(e -> ment(ugyfelRepository));
        betolt.addClickListener(e -> betolt(ugyfelRepository.findAll()));
        szamol.addClickListener(e -> db.setValue(new Integer(ugyfelRepository.findAll().size()).toString()));
    }

    private void ment(UgyfelRepository ugyfelRepository){
        ugyfelRepository.save(createUgyfel(nev.getValue(), telefon.getValue(), email.getValue()));
        nev.setValue("");
        telefon.setValue("");
        email.setValue("");
    }

    private void betolt(Collection<UgyfelEntity> lista){
        ArrayList<UgyfelEntity> ugyfelek = new ArrayList();
        ugyfelek.addAll(lista);
        add(nev2, telefon2, email2);
        UgyfelEntity ugyfel=ugyfelek.get(ugyfelek.size()-1);
        nev2.setValue(ugyfel.getNev());
        telefon2.setValue(ugyfel.getTelefon());
        email2.setValue(ugyfel.getEmail());
    }

    private UgyfelEntity createUgyfel (String nev, String telefon, String email){
        UgyfelEntity ugyfel= new UgyfelEntity();
        ugyfel.setTelefon(telefon);
        ugyfel.setNev(nev);
        ugyfel.setEmail(email);

        return ugyfel;
    }

}
