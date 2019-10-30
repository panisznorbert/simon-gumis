package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NyitvatartasModositasa extends VerticalLayout {

    private Label nyitvatartasModositoCim = new Label("Nyitvatartás módosítása:");
    private DatePicker datumok = new MagyarDatum("Dátum:");
    private Button nyitvatartas = new Button("Nyitvatartás módosítása");

    public NyitvatartasModositasa(){
        this.add(nyitvatartasModositoCim, datumok);
        nyitvatartas.addClickListener(e -> nyitvatartasModosito());
    }


    private void nyitvatartasModosito(){

        Dialog nyitvatartasiIdo = new Dialog();
        Button modosit = new Button("Módosít");
        Button megse = new Button("Mégse");
        HorizontalLayout kivalasztas = new HorizontalLayout();
        HorizontalLayout gombsor = new HorizontalLayout(megse, modosit);

        List<LocalTime> orak = new ArrayList<>();
        for (int i = 5; i < 20; i++) {
            orak.add(LocalTime.of(i, 0));
            orak.add(LocalTime.of(i, 30));
        }
        Collections.sort(orak);

        ComboBox<LocalTime> tol = new ComboBox<>("Nyitás", orak);
        ComboBox<LocalTime> ig = new ComboBox<>("Zárás", orak);
        kivalasztas.add(tol, ig);

        megse.addClickListener(e -> nyitvatartasiIdo.close());
        modosit.addClickListener(e -> {
            nyitvatartasiIdo.close();
        });

        nyitvatartasiIdo.add(kivalasztas, gombsor);
        nyitvatartasiIdo.open();
    }
}
