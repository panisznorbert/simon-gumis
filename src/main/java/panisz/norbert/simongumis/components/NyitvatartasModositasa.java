package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;
import panisz.norbert.simongumis.entities.NyitvatartasEntity;
import panisz.norbert.simongumis.services.NyitvatartasService;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class NyitvatartasModositasa extends VerticalLayout {

    private DatePicker datum = new MagyarDatum("Dátum:");
    private ComboBox<LocalTime> tol = new ComboBox<>("Nyitás");
    private ComboBox<LocalTime> ig = new ComboBox<>("Zárás");

    private Label informacio = new Label();

    private NyitvatartasService alapNyitvatartasService;

    private NyitvatartasEntity nyitvatartasEntity = new NyitvatartasEntity();

    NyitvatartasModositasa(NyitvatartasService nyitvatartasService){

        this.setAlignItems(Alignment.CENTER);

        datum.setMin(LocalDate.now());
        datum.setValue(LocalDate.now());

        datum.setRequired(true);

        alapNyitvatartasService = nyitvatartasService;

        datum.addValueChangeListener(e -> datumkivalasztas(e.getValue()));
        List<LocalTime> orak = new ArrayList<>();
        for (int i = 0; i < 23; i++) {
            orak.add(LocalTime.of(i, 0));
            orak.add(LocalTime.of(i, 30));
        }
        Collections.sort(orak);

        tol.setItems(orak);
        ig.setItems(orak);

        datumBeallitas();

        Button nyitvatartas = new Button("Nyitvatartás módosítása");
        Button zarva = new Button("Zárva");
        HorizontalLayout gombsor = new HorizontalLayout(nyitvatartas, zarva);
        Label cim = new Label("Nyitvatartás módosítása:");
        cim.getStyle().set("font-weight", "bold");
        this.add(cim, new HorizontalLayout(datum, tol, ig), informacio, gombsor);
        nyitvatartas.addClickListener(e -> nyitvatartasMentese());
        zarva.addClickListener(e -> zarvaMentese());
    }

    private void datumBeallitas(){
        nyitvatartasEntity = new NyitvatartasEntity();
        NyitvatartasEntity kivalasztottDatum = alapNyitvatartasService.adottNapNyitvatartasa(datum.getValue());
        if(kivalasztottDatum != null){
            tol.setValue(kivalasztottDatum.getNyitas());
            ig.setValue(kivalasztottDatum.getZaras());
            informacio.setText(kivalasztottDatum.toString() + " nyitvatartás van jelenleg beállítva");
            informacio.setVisible(true);
            nyitvatartasEntity = kivalasztottDatum;
            return;
        }
        if(DayOfWeek.SUNDAY.equals(datum.getValue().getDayOfWeek())){
            tol.setValue(LocalTime.of(0,0));
            ig.setValue(LocalTime.of(0,0));
            return;
        }
        if(DayOfWeek.SATURDAY.equals(datum.getValue().getDayOfWeek())){
            tol.setValue(LocalTime.of(7,0));
            ig.setValue(LocalTime.of(12,0));
            return;
        }
        tol.setValue(LocalTime.of(7,0));
        ig.setValue(LocalTime.of(17,0));
    }

    private void datumkivalasztas(LocalDate localDate){
        nyitvatartasEntity = new NyitvatartasEntity();
        NyitvatartasEntity kivalasztottDatum = alapNyitvatartasService.adottNapNyitvatartasa(localDate);
        if(kivalasztottDatum != null){
            tol.setValue(kivalasztottDatum.getNyitas());
            ig.setValue(kivalasztottDatum.getZaras());
            informacio.setText(kivalasztottDatum.toString() + " nyitvatartás van jelenleg beállítva");
            informacio.setVisible(true);
            nyitvatartasEntity = kivalasztottDatum;
        }else{
            informacio.setText("");
            informacio.setVisible(false);
            datumBeallitas();
        }

    }

    private void nyitvatartasMentese(){
        if(tol.isEmpty() || ig.isEmpty() || datum.isEmpty()){
            Notification hiba = new Hibajelzes("Hiányos kitöltés");
            hiba.open();
            return;
        }
        if(DayOfWeek.SUNDAY.equals(datum.getValue().getDayOfWeek()) && tol.getValue().equals(LocalTime.of(0,0)) && ig.getValue().equals(LocalTime.of(0,0)) && !informacio.isVisible()){
            Notification hiba = new Hibajelzes("Vasárnap alapértelmezetten zárva van a műhely");
            hiba.open();
            return;
        }
        if(DayOfWeek.SATURDAY.equals(datum.getValue().getDayOfWeek()) && tol.getValue().equals(LocalTime.of(7,0)) && ig.getValue().equals(LocalTime.of(12,0)) && !informacio.isVisible()){
            Notification hiba = new Hibajelzes("Szombaton alapértelmezetten 7:00-12:00-ig van a műhely nyitva");
            hiba.open();
            return;
        }
        if(tol.getValue().equals(LocalTime.of(7,0)) && ig.getValue().equals(LocalTime.of(17,0)) && !informacio.isVisible()){
            Notification hiba = new Hibajelzes("Hétköznapokon alapértelmezetten 7:00-17:00-ig van a műhely nyitva");
            hiba.open();
            return;
        }
        nyitvatartasEntity.setDatum(datum.getValue());
        nyitvatartasEntity.setNyitas(tol.getValue());
        nyitvatartasEntity.setZaras(ig.getValue());
        if(tol.getValue().equals(LocalTime.of(0,0)) && ig.getValue().equals(LocalTime.of(0,0))){
            nyitvatartasEntity.setNyitva(false);
        }else{
            nyitvatartasEntity.setNyitva(true);
        }

        try{
            alapNyitvatartasService.ment(nyitvatartasEntity);
            UI.getCurrent().getPage().reload();
        }catch(Exception ex){
            Notification hiba = new Hibajelzes("Sikertelen mentés, próbálja újra");
            hiba.open();
        }

    }

    private void zarvaMentese(){
        if(datum.isEmpty()){
            Notification hiba = new Hibajelzes("Nincs megadva dátum");
            hiba.open();
            return;
        }
        if(DayOfWeek.SUNDAY.equals(datum.getValue().getDayOfWeek()) && !informacio.isVisible()){
            Notification hiba = new Hibajelzes("Vasárnap alapértelmezetten zárva van a műhely");
            hiba.open();
            return;
        }
        nyitvatartasEntity.setDatum(datum.getValue());
        nyitvatartasEntity.setNyitas(LocalTime.of(0,0));
        nyitvatartasEntity.setZaras(LocalTime.of(0,0));
        nyitvatartasEntity.setNyitva(false);
        try{
            alapNyitvatartasService.ment(nyitvatartasEntity);
            UI.getCurrent().getPage().reload();
        }catch(Exception ex){
            Notification hiba = new Hibajelzes("Sikertelen mentés, próbálja újra");
            hiba.open();
        }
    }
}
