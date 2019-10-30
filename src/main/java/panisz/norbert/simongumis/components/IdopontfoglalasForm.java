package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.UI;
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
import panisz.norbert.simongumis.entities.NyitvatartasEntity;
import panisz.norbert.simongumis.entities.UgyfelEntity;
import panisz.norbert.simongumis.services.IdopontfoglalasServie;
import panisz.norbert.simongumis.services.NyitvatartasService;
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


    public IdopontfoglalasForm(IdopontfoglalasServie idopontfoglalasServie, NyitvatartasService nyitvatartasService){
        this.idopontfoglalasServie = idopontfoglalasServie;
        idopontokDatum = new MagyarDatum("Dátum:");
        foglalhatoOrak = new ComboBox<>("Szabad időpontok");
        idopontok.add(idopontokDatum, foglalhatoOrak);
        add(idopontok, ugyfelAdatok, egyeb, gombsor);
        this.setAlignItems(Alignment.CENTER);
        idopontokDatum.addValueChangeListener(e -> kivalasztottDatum(e.getValue(), nyitvatartasService));
        foglal.addClickListener(e -> idopontFoglalas());
        alapBeallitas(nyitvatartasService);

    }


    private void alapBeallitas(NyitvatartasService nyitvatartasService){
        megjegyzes.setValue("");
        if(LocalTime.now().isBefore(LocalTime.of(16, 30))){
            idopontokDatum.setMin(LocalDate.now());
            idopontokDatum.setValue(LocalDate.now());
            orakFeltoltese(LocalDate.now(), nyitvatartasService);
        }else{
            idopontokDatum.setMin(LocalDate.now().plusDays(1));
            idopontokDatum.setValue(LocalDate.now().plusDays(1));
            orakFeltoltese(LocalDate.now().plusDays(1), nyitvatartasService);
        }
        idopontokDatum.setRequired(true);
        foglalhatoOrak.setRequired(true);
        ugyfelAdatok.alaphelyzet();
    }

    private void kivalasztottDatum(LocalDate kivalasztottDatum, NyitvatartasService nyitvatartasService){
        foglal.setEnabled(true);
        if(!idopontokDatum.isInvalid()){
            orakFeltoltese(kivalasztottDatum, nyitvatartasService);
            return;
        }

        foglalhatoOrak.clear();
        idopontok.remove(foglalhatoOrak);

    }

    private void orakFeltoltese(LocalDate kivalasztottDatum, NyitvatartasService nyitvatartasService){
        idopontok.remove(foglalhatoOrak);
        if(DayOfWeek.SUNDAY.equals(kivalasztottDatum.getDayOfWeek())){
            idopontokDatum.setInvalid(true);
            Notification hibaAblak = new Hibajelzes("Vasárnap zárva vagyunk.");
            hibaAblak.open();
            foglal.setEnabled(false);
            return;
        }

        int munkaidoKezdete = 7;
        int munkaidoVege = 17;

        LocalDate viszonyitasDate = LocalDate.now();
        LocalTime viszonyitasTime = LocalTime.now();


        //szombaton csak délig van nyitva
        if (DayOfWeek.SATURDAY.equals(kivalasztottDatum.getDayOfWeek())) {
            munkaidoVege = 12;
        }

        //Amennyiben van eltérő nyitvatartás az kerül beállításra
        boolean nyitvatartasFelig = false;
        NyitvatartasEntity nyitvatartasEntity = nyitvatartasService.adottNapNyitvatartasa(kivalasztottDatum);
        if(nyitvatartasEntity != null){
            if(!nyitvatartasEntity.isNyitva()) {
                idopontokDatum.setInvalid(true);
                Notification hibaAblak = new Hibajelzes(nyitvatartasEntity.toString() + " vagyunk");
                hibaAblak.open();
                return;
            }

            viszonyitasDate = nyitvatartasEntity.getDatum();
            viszonyitasTime = nyitvatartasEntity.getNyitas();

            munkaidoKezdete = nyitvatartasEntity.getNyitas().getHour();
            munkaidoVege = nyitvatartasEntity.getZaras().getHour();

            if(nyitvatartasEntity.getZaras().getMinute() == 30){
                nyitvatartasFelig = true;
            }

        }

        List<LocalTime> orak = new ArrayList<>();

        if(LocalDate.now().equals(viszonyitasDate) && LocalTime.now().isAfter(viszonyitasTime)){
            viszonyitasTime = LocalTime.now();
        }

        for (int i = munkaidoKezdete; i < munkaidoVege; i++) {
            if (idopontfoglalasServie.keresesDatumra(LocalDateTime.of(kivalasztottDatum, LocalTime.of(i, 0))) == null && !(viszonyitasDate.equals(kivalasztottDatum) && (viszonyitasTime.isAfter(LocalTime.of(i, 0)))) ) {
                orak.add(LocalTime.of(i, 0));
            }
            if (idopontfoglalasServie.keresesDatumra(LocalDateTime.of(kivalasztottDatum, LocalTime.of(i, 30))) == null && !(viszonyitasDate.equals(kivalasztottDatum) && (viszonyitasTime.isAfter(LocalTime.of(i, 30))))) {
                orak.add(LocalTime.of(i, 30));
            }
        }

        if(nyitvatartasFelig){
            if (idopontfoglalasServie.keresesDatumra(LocalDateTime.of(kivalasztottDatum, LocalTime.of(munkaidoVege, 0))) == null && !(viszonyitasDate.equals(kivalasztottDatum) && (viszonyitasTime.isAfter(LocalTime.of(munkaidoVege, 0))))) {
                orak.add(LocalTime.of(munkaidoVege, 0));
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
                IdopontfoglalasEntity foglalas = idopontfoglalasServie.ment(idopontFoglalasAdat());
                String idopont = foglalas.getDatum().toString();
                idopont = idopont.replaceAll("T", " ");

                Notification visszajelzes = new Hibajelzes("Az időpontfoglalás sikeresen megtörtént " + idopont + " időpontra");
                visszajelzes.open();
                UI.getCurrent().navigate("gumik");
            }catch(Exception ex){
                Notification hibaAblak = new Hibajelzes(ex.getMessage());
                hibaAblak.open();
            }

        }
    }
}
