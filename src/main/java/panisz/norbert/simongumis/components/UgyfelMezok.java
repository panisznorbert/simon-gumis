package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import panisz.norbert.simongumis.entities.UgyfelEntity;
import panisz.norbert.simongumis.exceptions.HibasKitoltesException;

@EqualsAndHashCode(callSuper = true)
@Data
class UgyfelMezok extends VerticalLayout {
    private TextField nev = new TextField("Név:");
    private TextField telefon = new TextField("Telefon:");
    private TextField email = new TextField("E-mail:");

    private Checkbox pipa = new Checkbox();
    private Label leiras = new Label("Elfogadom az oldal adatkezelését");
    private Icon adatkezelesLeiras = new Icon(VaadinIcon.FILE);
    private HorizontalLayout adatkezeles = new HorizontalLayout(pipa, leiras, adatkezelesLeiras);

    UgyfelMezok(){
        adatkezelesLeiras.addClickListener(e -> adatkezelesMegnyitas());
        adatkezeles.setAlignItems(Alignment.BASELINE);
        this.setAlignItems(Alignment.CENTER);
        adatkezeles.getStyle().set("z-index", "10");

        pipa.addClickListener(e -> {
            leiras.getStyle().set("font-color", "black");
        });

        Binder<UgyfelEntity> binder = new Binder<>();
        binder.forField(nev)
                .asRequired("Kitöltendő")
                .bind(UgyfelEntity::getNev, UgyfelEntity::setNev);
        binder.forField(telefon)
                .withValidator(telefon -> telefon.length() >= 9, "Hibás telefonszám")
                .asRequired("Kitöltendő")
                .bind(UgyfelEntity::getTelefon, UgyfelEntity::setTelefon);
        binder.forField(email)
                .withValidator(new EmailValidator("Hibás e-mail cím"))
                .asRequired("Kitöltendő")
                .bind(UgyfelEntity::getEmail, UgyfelEntity::setEmail);
        add(new HorizontalLayout(nev, telefon, email), adatkezeles);
    }

    private void adatkezelesMegnyitas(){
        Dialog szoveg = new Dialog();
        szoveg.open();
    }

    void kitoltottseg() throws HibasKitoltesException {
        if(!pipa.getValue()){
            throw new HibasKitoltesException("Nem fogadta el az adatkezelést");
        }
        if(nev.isInvalid() || nev.isEmpty()){
            throw new HibasKitoltesException("Nem adott meg nevet");
        }
        if(telefon.isInvalid() || telefon.isEmpty() || telefon.isPreventInvalidInput()){
            throw new HibasKitoltesException("Nem megfelelő telefonszám");
        }
        if(email.isInvalid() || email.isEmpty() || email.isPreventInvalidInput()){
            throw new HibasKitoltesException("Nem megfelelő e-mail cím");
        }
    }

    void alaphelyzet(){
        nev.clear();
        telefon.clear();
        email.clear();
    }
}
