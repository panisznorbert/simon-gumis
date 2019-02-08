package panisz.norbert.simongumis.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import panisz.norbert.simongumis.LoggerExample;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class IdopontFoglalasView extends VerticalLayout {

    private final static Logger LOGGER = Logger.getLogger(LoggerExample.class.getName());
    private DatePicker idopontokDatum = new DatePicker("Dátum:");
    ComboBox foglalhatoOrak = new ComboBox("Szabad időpontok");
    private HorizontalLayout idopontok = new HorizontalLayout(idopontokDatum, foglalhatoOrak);
    private Button foglal = new Button("Lefoglal");
    private HorizontalLayout gombsor = new HorizontalLayout(foglal);
    private TextField nev = new TextField("Név:");
    private TextField telefon = new TextField("Telefon:");
    private TextField email = new TextField("E-mail:");
    private HorizontalLayout adatok = new HorizontalLayout(nev, telefon, email);
    private TextField megjegyzes = new TextField("Megjegyzés:");
    private VerticalLayout torzs = new VerticalLayout(adatok, megjegyzes);


    public IdopontFoglalasView(){
        init();
        idopontokDatum.addValueChangeListener(e -> orakFeltoltese());
    }

    private void init(){
        add(idopontok, torzs, gombsor);
    }

    private void orakFeltoltese(){
        idopontok.remove(foglalhatoOrak);
        List orak = new ArrayList();
        for(Integer i=8;i<17;i++){
            orak.add(i.toString() + ":00");
            orak.add(i.toString() + ":30");
        }
        foglalhatoOrak = new ComboBox("Szabad időpontok", orak);
        idopontok.add(foglalhatoOrak);
    }
}
