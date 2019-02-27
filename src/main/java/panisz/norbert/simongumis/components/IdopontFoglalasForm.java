package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import panisz.norbert.simongumis.LoggerExample;
import panisz.norbert.simongumis.entities.FoglalasEntity;
import panisz.norbert.simongumis.entities.UgyfelEntity;
import panisz.norbert.simongumis.repositories.FoglalasRepository;
import panisz.norbert.simongumis.repositories.UgyfelRepository;
import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.logging.Logger;

@UIScope
@Component
public class IdopontFoglalasForm extends VerticalLayout {
    @Autowired
    private FoglalasRepository foglalasRepository;
    @Autowired
    private UgyfelRepository ugyfelRepository;
    private MenuForm fomenu = new MenuForm();
    private final static Logger LOGGER = Logger.getLogger(LoggerExample.class.getName());
    private DatePicker idopontokDatum;
    private ComboBox<LocalTime> foglalhatoOrak;
    private HorizontalLayout idopontok = new HorizontalLayout();

    private Button foglal = new Button("Lefoglal");
    private HorizontalLayout gombsor = new HorizontalLayout(foglal);

    private UgyfelMezok ugyfelAdatok = new UgyfelMezok();

    private TextField megjegyzes = new TextField("Megjegyzés:");


    public IdopontFoglalasForm(){
        init();
    }

    private DatePicker.DatePickerI18n magyarDatumInit(){
        DatePicker.DatePickerI18n magyarDatum = new DatePicker.DatePickerI18n();
        magyarDatum.setCalendar("Kalendárium");
        magyarDatum.setCancel("Mégse");
        magyarDatum.setClear("Ürít");
        magyarDatum.setFirstDayOfWeek(1);
        String[] honap = {"Január", "Február", "Március", "Április", "Május", "Június", "Július", "Augusztus", "Szeptember", "Október", "November", "December"};
        magyarDatum.setMonthNames(Arrays.asList(honap));
        magyarDatum.setToday("Ma");
        magyarDatum.setWeek("Hét");
        String[] nap = {"Vasárnap", "Hétfő", "Kedd", "Szerda", "Csütörtök", "Péntek", "Szombat"};
        magyarDatum.setWeekdays(Arrays.asList(nap));
        String[] napRov = {"Va", "Hé", "Ke", "Sze", "Csü", "Pé", "Szo"};
        magyarDatum.setWeekdaysShort(Arrays.asList(napRov));

        return magyarDatum;
    }

    private void init(){
        idopontokDatum = new DatePicker("Dátum:");
        foglalhatoOrak = new ComboBox<>("Szabad időpontok");
        idopontok.add(idopontokDatum, foglalhatoOrak);
        add(fomenu, idopontok, ugyfelAdatok, megjegyzes, gombsor);
        idopontokDatum.addValueChangeListener(e -> orakFeltoltese(e.getValue()));
        foglal.addClickListener(e -> idopontFoglalas());
    }

    @PostConstruct
    private void alapBeallitas(){
        megjegyzes.setValue("");
        idopontokDatum.setMin(LocalDate.now());
        idopontokDatum.setRequired(true);
        foglalhatoOrak.setRequired(true);
        idopontokDatum.setValue(LocalDate.now());
        idopontokDatum.setI18n(magyarDatumInit());
        orakFeltoltese(LocalDate.now());
    }

    private void orakFeltoltese(LocalDate kivalasztottDatum){
        idopontok.remove(foglalhatoOrak);
        List<LocalTime> orak = new ArrayList<>();

        for(int i=8; i<17; i++){
            if(foglalasRepository.findByDatum(LocalDateTime.of(kivalasztottDatum, LocalTime.of(i, 0))) == null && !(LocalDate.now().equals(kivalasztottDatum) && (LocalTime.now().isAfter(LocalTime.of(i, 0))))){
                orak.add(LocalTime.of(i, 0));
            }
            if(foglalasRepository.findByDatum(LocalDateTime.of(kivalasztottDatum, LocalTime.of(i, 30))) == null && !(LocalDate.now().equals(kivalasztottDatum) && (LocalTime.now().isAfter(LocalTime.of(i, 30))))){
                orak.add(LocalTime.of(i, 30));
            }
        }
        Collections.sort(orak);
        foglalhatoOrak = new ComboBox<>("Szabad időpontok", orak);
        idopontok.add(foglalhatoOrak);
    }

    private FoglalasEntity idopontFoglalasAdat(){
        FoglalasEntity foglalasEntity = new FoglalasEntity();

        UgyfelEntity ugyfelEntity = ugyfelRepository.findByNevAndTelefonAndEmail(ugyfelAdatok.getNev().getValue(), ugyfelAdatok.getTelefon().getValue(), ugyfelAdatok.getEmail().getValue());

        if(ugyfelEntity == null) {
            ugyfelEntity = new UgyfelEntity();
            ugyfelEntity.setNev(ugyfelAdatok.getNev().getValue());
            ugyfelEntity.setTelefon(ugyfelAdatok.getTelefon().getValue());
            ugyfelEntity.setEmail(ugyfelAdatok.getEmail().getValue());
        }

        foglalasEntity.setUgyfel(ugyfelEntity);

        foglalasEntity.setDatum(LocalDateTime.of(idopontokDatum.getValue(), foglalhatoOrak.getValue()));

        foglalasEntity.setMegjegyzes(megjegyzes.getValue());

        return foglalasEntity;
    }

    private boolean kitoltottseg(){
        return idopontokDatum.isEmpty() || foglalhatoOrak.isEmpty() || ugyfelAdatok.kitoltottseg();
    }

    private void idopontFoglalas(){
        if(kitoltottseg()){
            Notification hiba = new HibaJelzes("Hibás kitöltés");
            hiba.open();
        }else{
            foglalasRepository.save(idopontFoglalasAdat());
            LOGGER.info("Mentett Id: " + ugyfelRepository.findByNevAndTelefonAndEmail(ugyfelAdatok.getNev().getValue(), ugyfelAdatok.getTelefon().getValue(), ugyfelAdatok.getEmail().getValue()).getId().toString());
            alaphelyzetbeAllit();
        }
    }

    private void alaphelyzetbeAllit(){
        idopontokDatum.setMin(LocalDate.now());
        idopontokDatum.setValue(LocalDate.now());
        orakFeltoltese(LocalDate.now());
        ugyfelAdatok.alaphelyzet();
        megjegyzes.setValue("");
        idopontokDatum.setMin(LocalDate.now());
    }
}
