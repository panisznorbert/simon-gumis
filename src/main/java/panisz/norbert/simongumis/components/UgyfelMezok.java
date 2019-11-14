package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;
import panisz.norbert.simongumis.entities.UgyfelEntity;

@EqualsAndHashCode(callSuper = true)
@Data
class UgyfelMezok extends HorizontalLayout {
    private TextField nev = new TextField("Név:");
    private TextField telefon = new TextField("Telefon:");
    private TextField email = new TextField("E-mail:");

    UgyfelMezok(){
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
        add(nev, telefon, email);
    }

    boolean kitoltottseg(){
        return nev.isInvalid() || telefon.isInvalid() || email.isInvalid() || nev.isEmpty() || telefon.isEmpty() || email.isEmpty() || email.isPreventInvalidInput() || telefon.isPreventInvalidInput();
    }

    void alaphelyzet(){
        nev.clear();
        telefon.clear();
        email.clear();
    }
}
