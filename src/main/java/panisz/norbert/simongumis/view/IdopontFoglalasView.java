package panisz.norbert.simongumis.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import panisz.norbert.simongumis.LoggerExample;
import panisz.norbert.simongumis.entities.FoglalasEntity;
import panisz.norbert.simongumis.entities.UgyfelEntity;
import panisz.norbert.simongumis.repositories.FoglalasRepository;
import panisz.norbert.simongumis.repositories.UgyfelRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;


public class IdopontFoglalasView extends VerticalLayout {

    private final static Logger LOGGER = Logger.getLogger(LoggerExample.class.getName());
    private DatePicker idopontokDatum = new DatePicker("Dátum:");
    private ComboBox<LocalTime> foglalhatoOrak = new ComboBox<>("Szabad időpontok");
    private HorizontalLayout idopontok = new HorizontalLayout(idopontokDatum, foglalhatoOrak);
    private Button foglal = new Button("Lefoglal");
    private HorizontalLayout gombsor = new HorizontalLayout(foglal);
    private TextField nev = new TextField("Név:");
    private TextField telefon = new TextField("Telefon:");
    private TextField email = new TextField("E-mail:");
    private HorizontalLayout adatok = new HorizontalLayout(nev, telefon, email);
    private TextField megjegyzes = new TextField("Megjegyzés:");
    private VerticalLayout torzs = new VerticalLayout(adatok, megjegyzes);

    private FoglalasRepository alapFoglalasRepository;
    private UgyfelRepository alapUgyfelRepository;

    public IdopontFoglalasView(FoglalasRepository foglalasRepository, UgyfelRepository ugyfelRepository){
        alapFoglalasRepository = foglalasRepository;
        alapUgyfelRepository = ugyfelRepository;
        init();
        idopontokDatum.addValueChangeListener(e -> orakFeltoltese(e.getValue()));
        foglal.addClickListener(e -> idopontFoglalas());
    }

    private void init(){
        idopontokDatum.setMin(LocalDate.now());
        idopontokDatum.setValue(LocalDate.now());
        orakFeltoltese(LocalDate.now());
        idopontokDatum.setRequired(true);
        foglalhatoOrak.setRequired(true);
        nev.setRequired(true);
        telefon.setRequired(true);
        email.setRequired(true);
        megjegyzes.setValue("");
        add(idopontok, torzs, gombsor);
    }

    private void orakFeltoltese(LocalDate kivalasztottDatum){
        idopontok.remove(foglalhatoOrak);
        List<LocalTime> orak = new ArrayList<>();

        for(int i=8; i<17; i++){
            if(alapFoglalasRepository.findByDatum(LocalDateTime.of(kivalasztottDatum, LocalTime.of(i, 0))) == null && !(LocalDate.now().equals(kivalasztottDatum) && (LocalTime.now().isAfter(LocalTime.of(i, 0))))){
                orak.add(LocalTime.of(i, 0));
            }
            if(alapFoglalasRepository.findByDatum(LocalDateTime.of(kivalasztottDatum, LocalTime.of(i, 30))) == null && !(LocalDate.now().equals(kivalasztottDatum) && (LocalTime.now().isAfter(LocalTime.of(i, 30))))){
                orak.add(LocalTime.of(i, 30));
            }
        }
        Collections.sort(orak);
        foglalhatoOrak = new ComboBox<>("Szabad időpontok", orak);
        idopontok.add(foglalhatoOrak);
    }

    private FoglalasEntity idopontFoglalasAdat(){
        FoglalasEntity foglalasEntity = new FoglalasEntity();

        UgyfelEntity ugyfelEntity = alapUgyfelRepository.findByNevAndTelefonAndEmail(nev.getValue(), telefon.getValue(), email.getValue());

        if(ugyfelEntity == null) {
            ugyfelEntity = new UgyfelEntity();
            ugyfelEntity.setNev(nev.getValue());
            ugyfelEntity.setTelefon(telefon.getValue());
            ugyfelEntity.setEmail(email.getValue());
        }

        foglalasEntity.setUgyfel(ugyfelEntity);

        foglalasEntity.setDatum(LocalDateTime.of(idopontokDatum.getValue(), foglalhatoOrak.getValue()));

        foglalasEntity.setMegjegyzes(megjegyzes.getValue());

        return foglalasEntity;
    }

    private boolean kitoltottseg(){
        return idopontokDatum.isEmpty() || foglalhatoOrak.isEmpty()
                || nev.isInvalid() || telefon.isInvalid() || email.isInvalid()
                || nev.isEmpty() || telefon.isEmpty() || email.isEmpty();
    }

    private void idopontFoglalas(){
        if(kitoltottseg()){
            Notification hiba = new HibaView("Hibás kitöltés");
            hiba.open();
        }else{
            alapFoglalasRepository.save(idopontFoglalasAdat());
            LOGGER.info("Mentett Id: " + alapUgyfelRepository.findByNevAndTelefonAndEmail(nev.getValue(), telefon.getValue(), email.getValue()).getId().toString());
            alaphelyzetbeAllit();

        }
    }

    private void alaphelyzetbeAllit(){
        idopontokDatum.setMin(LocalDate.now());
        idopontokDatum.setValue(LocalDate.now());
        orakFeltoltese(LocalDate.now());
        nev.clear();
        telefon.clear();
        email.clear();
        megjegyzes.setValue("");
        idopontokDatum.setMin(LocalDate.now());
    }
}
