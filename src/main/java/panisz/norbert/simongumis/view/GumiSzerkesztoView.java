package panisz.norbert.simongumis.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import panisz.norbert.simongumis.entities.GumikEntity;

public class GumiSzerkesztoView extends VerticalLayout {

    private TextField gyarto = new TextField("Gyártó");
    private TextField meret = new TextField("Méret");
    private TextField ar = new TextField("Ár");
    private ComboBox evszak = new ComboBox("Évszak", "Téli", "Nyári");
    private ComboBox allapot = new ComboBox("Állapot", "Új","Használt");
    private TextField darab  = new TextField("Raktárkészlet");

    private Button ment  = new Button("Mentés");
    private Button megse  = new Button("Mégse");

    public GumiSzerkesztoView(GumikEntity gumikEntity){

        gyarto.setValue(gumikEntity.getGyarto());
        meret.setValue(gumikEntity.getMeret());
        evszak.setValue(gumikEntity.getEvszak());
        allapot.setValue(gumikEntity.getAllapot());
        ar.setValue(gumikEntity.getAr().toString());
        darab.setValue(gumikEntity.getMennyisegRaktarban().toString());

        add(gyarto, meret, evszak, allapot, ar, darab);

    }

    public GumikEntity beallit(GumikEntity gumikEntity){
        gumikEntity.setGyarto(gyarto.getValue());
        gumikEntity.setMeret(meret.getValue());
        gumikEntity.setEvszak(evszak.getValue().toString());
        gumikEntity.setAllapot(allapot.getValue().toString());
        gumikEntity.setAr(Float.valueOf(ar.getValue()));
        gumikEntity.setMennyisegRaktarban(Integer.valueOf(darab.getValue()));

        return gumikEntity;
    }
}
