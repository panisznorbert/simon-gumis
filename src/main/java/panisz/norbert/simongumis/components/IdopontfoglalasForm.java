package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;
import panisz.norbert.simongumis.entities.IdopontfoglalasEntity;
import panisz.norbert.simongumis.entities.UgyfelEntity;
import panisz.norbert.simongumis.services.IdopontfoglalasServie;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@UIScope
@Component
public class IdopontfoglalasForm extends VerticalLayout {

    private IdopontfoglalasServie idopontfoglalasServie;

    private DatePicker idopontokDatum;
    private ComboBox<LocalTime> foglalhatoOrak;
    private HorizontalLayout idopontok = new HorizontalLayout();

    private Button foglal = new Button("Lefoglal");
    private HorizontalLayout gombsor = new HorizontalLayout(foglal);

    private UgyfelMezok ugyfelAdatok = new UgyfelMezok();

    private TextField megjegyzes = new TextField("Megjegyzés:");
    private HorizontalLayout egyeb = new HorizontalLayout(megjegyzes);


    public IdopontfoglalasForm(IdopontfoglalasServie idopontfoglalasServie){
        this.idopontfoglalasServie = idopontfoglalasServie;
        idopontokDatum = new DatePicker("Dátum:");
        foglalhatoOrak = new ComboBox<>("Szabad időpontok");
        idopontok.add(idopontokDatum, foglalhatoOrak);
        add(idopontok, ugyfelAdatok, egyeb, gombsor);
        this.setAlignItems(Alignment.CENTER);
        idopontokDatum.addValueChangeListener(e -> kivalasztottDatum(e.getValue()));
        foglal.addClickListener(e -> idopontFoglalas());

        alapBeallitas();

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



    private void alapBeallitas(){
        megjegyzes.setValue("");
        if(LocalTime.now().isBefore(LocalTime.of(16, 30))){
            idopontokDatum.setMin(LocalDate.now());
            idopontokDatum.setValue(LocalDate.now());
            orakFeltoltese(LocalDate.now());
        }else{
            idopontokDatum.setMin(LocalDate.now().plusDays(1));
            idopontokDatum.setValue(LocalDate.now().plusDays(1));
            orakFeltoltese(LocalDate.now().plusDays(1));
        }
        idopontokDatum.setRequired(true);
        foglalhatoOrak.setRequired(true);
        idopontokDatum.setI18n(magyarDatumInit());
        ugyfelAdatok.alaphelyzet();
    }

    private void kivalasztottDatum(LocalDate kivalasztottDatum){
        if(!idopontokDatum.isInvalid()){
            orakFeltoltese(kivalasztottDatum);
            return;
        }

        foglalhatoOrak.clear();
        idopontok.remove(foglalhatoOrak);

    }

    private void orakFeltoltese(LocalDate kivalasztottDatum){
        idopontok.remove(foglalhatoOrak);
        if(DayOfWeek.SUNDAY.equals(kivalasztottDatum.getDayOfWeek())){
            idopontokDatum.setInvalid(true);
            Notification hibaAblak = new Hibajelzes("Vasárnap zárva vagyunk.");
            hibaAblak.open();
            return;
        }

        List<LocalTime> orak = new ArrayList<>();
        int munkaidoVege = 17;
        if (DayOfWeek.SATURDAY.equals(kivalasztottDatum.getDayOfWeek())) {
                munkaidoVege = 12;
            }
        for (int i = 7; i < munkaidoVege; i++) {
            if (idopontfoglalasServie.keresesDatumra(LocalDateTime.of(kivalasztottDatum, LocalTime.of(i, 0))) == null && !(LocalDate.now().equals(kivalasztottDatum) && (LocalTime.now().isAfter(LocalTime.of(i, 0))))) {
                orak.add(LocalTime.of(i, 0));
            }
            if (idopontfoglalasServie.keresesDatumra(LocalDateTime.of(kivalasztottDatum, LocalTime.of(i, 30))) == null && !(LocalDate.now().equals(kivalasztottDatum) && (LocalTime.now().isAfter(LocalTime.of(i, 30))))) {
                orak.add(LocalTime.of(i, 30));
            }
        }
        Collections.sort(orak);
        foglalhatoOrak = new ComboBox<>("Szabad időpontok", orak);
        idopontok.add(foglalhatoOrak);

    }

    private IdopontfoglalasEntity idopontFoglalasAdat(){
        IdopontfoglalasEntity idopontFoglalasEntity = new IdopontfoglalasEntity();
        UgyfelEntity ugyfelEntity = new UgyfelEntity();

        ugyfelEntity.setNev(ugyfelAdatok.getNev().getValue());
        ugyfelEntity.setTelefon(ugyfelAdatok.getTelefon().getValue());
        ugyfelEntity.setEmail(ugyfelAdatok.getEmail().getValue());

        idopontFoglalasEntity.setUgyfel(ugyfelEntity);
        idopontFoglalasEntity.setDatum(LocalDateTime.of(idopontokDatum.getValue(), foglalhatoOrak.getValue()));
        idopontFoglalasEntity.setMegjegyzes(megjegyzes.getValue());

        return idopontFoglalasEntity;
    }

    private boolean kitoltottseg(){
        return idopontokDatum.isEmpty() || foglalhatoOrak.isEmpty() || ugyfelAdatok.kitoltottseg();
    }

    private void idopontFoglalas(){
        if(kitoltottseg()){
            Notification hiba = new Hibajelzes("Hibás kitöltés. A megjegyzésen kívül minden mező kitöltése kötelező!");
            hiba.open();
        }else{
            try{
                idopontfoglalasServie.ment(idopontFoglalasAdat());
                alapBeallitas();
            }catch(Exception ex){
                Notification hibaAblak = new Hibajelzes(ex.getMessage());
                hibaAblak.open();
            }

        }
    }
}
