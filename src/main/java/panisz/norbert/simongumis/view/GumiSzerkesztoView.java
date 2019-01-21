package panisz.norbert.simongumis.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import panisz.norbert.simongumis.entities.GumikEntity;


public class GumiSzerkesztoView extends VerticalLayout {

    private TextField gyarto = new TextField("Gyártó");
    private TextField meret1 = new TextField("Méret-szélesség");
    private TextField meret2 = new TextField("Méret-profil arány");
    private TextField meret3 = new TextField("Méret-felni átmérő");
    private TextField ar = new TextField("Ár");
    private ComboBox evszak = new ComboBox("Évszak", "Téli", "Nyári");
    private ComboBox allapot = new ComboBox("Állapot", "Új","Használt");
    private TextField darab  = new TextField("Raktárkészlet");


    public GumiSzerkesztoView(GumikEntity gumikEntity){

        ar.setPattern("\\d*(\\.\\d*)?");
        ar.setPreventInvalidInput(true);
        ar.setSuffixComponent(new Span("Ft"));

        darab.setPattern("[0-9]*");
        darab.setPreventInvalidInput(true);
        darab.setSuffixComponent(new Span("Db"));

        gyarto.setRequired(true);
        ar.setRequired(true);
        meret1.setRequired(true);
        meret2.setRequired(true);
        meret3.setRequired(true);
        evszak.setRequired(true);
        allapot.setRequired(true);
        darab.setRequired(true);

        String[] meret = gumikEntity.getMeret().split("/");

        gyarto.setValue(gumikEntity.getGyarto());
        meret1.setValue(meret[0]);
        meret2.setValue(meret[1]);
        meret3.setValue(meret[3]);
        evszak.setValue(gumikEntity.getEvszak());
        allapot.setValue(gumikEntity.getAllapot());
        ar.setValue(gumikEntity.getAr().toString());
        darab.setValue(gumikEntity.getMennyisegRaktarban().toString());

        add(gyarto, meret1, meret2, meret3, evszak, allapot, ar, darab);
    }

    public GumikEntity beallit(GumikEntity gumikEntity){
            gumikEntity.setGyarto(gyarto.getValue());
            gumikEntity.setMeret(meret1.getValue() + "/" + meret2.getValue() + "/R/" + meret3.getValue());
            gumikEntity.setEvszak(evszak.getValue().toString());
            gumikEntity.setAllapot(allapot.getValue().toString());
            gumikEntity.setAr(Float.valueOf(ar.getValue()));
            gumikEntity.setMennyisegRaktarban(Integer.valueOf(darab.getValue()));

        return gumikEntity;
    }

    public String validacio() {
        return GumikView.getString(gyarto, evszak, allapot, ar, darab, meret1, meret2, meret3);
    }




}
