package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.*;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;
import panisz.norbert.simongumis.entities.IdopontfoglalasEntity;
import panisz.norbert.simongumis.services.IdopontfoglalasServie;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

import static java.util.Collections.sort;

@StyleSheet("lefoglaltIdopontok.css")
@UIScope
@Component
public class LefoglaltIdopontokForm extends VerticalLayout {

    private IdopontfoglalasServie alapFoglalasService;

    private Grid<IdopontfoglalasEntity> tablaMa = new Grid<>();

    private Grid<IdopontfoglalasEntity> tabla = new Grid<>();

    public LefoglaltIdopontokForm(IdopontfoglalasServie foglalasService){
        this.alapFoglalasService = foglalasService;
        this.setAlignItems(Alignment.CENTER);
        this.setId("lefoglalt-idopontok");
        tabla.setHeightByRows(true);
        tabla.setVerticalScrollingEnabled(false);
        tablaMa.setHeightByRows(true);
        tablaMa.setVerticalScrollingEnabled(false);
        tabla.setId("tabla-tobbi");
        tablaMa.setId("tabla-ma");
        List<IdopontfoglalasEntity> maiFoglalasok = alapFoglalasService.keresesMa();
        VerticalLayout foglalasok = new VerticalLayout();
        if(maiFoglalasok != null && !maiFoglalasok.isEmpty()){
            tablafeltolto(maiFoglalasok, tablaMa);
            foglalasok.add(tablaMa);
        }
        List<IdopontfoglalasEntity> tobbiFoglalasok = alapFoglalasService.keresesNaptol(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(0,0)));;
        if(tobbiFoglalasok != null && !tobbiFoglalasok.isEmpty()){
            tablafeltolto(tobbiFoglalasok, tabla);
            foglalasok.add(tabla);
        }

        add(new HorizontalLayout(foglalasok));
    }

    private void tablafeltolto(List<IdopontfoglalasEntity> foglalasok, Grid<IdopontfoglalasEntity> tabla){
        sort(foglalasok);

        tabla.addColumn(new LocalDateTimeRenderer<>(IdopontfoglalasEntity::getDatum, DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)), "Időpont").setHeader("Időpont").setWidth("180px");
        tabla.addColumn(TemplateRenderer.<IdopontfoglalasEntity> of(
                "<div><small>Név: [[item.nev]]<br>Tel: [[item.telefon]]<br>E-mail: [[item.email]]</small></div>")
                        .withProperty("nev",
                                foglalasEntity -> foglalasEntity.getUgyfel().getNev())
                        .withProperty("telefon",
                                foglalasEntity -> foglalasEntity.getUgyfel().getTelefon())
                        .withProperty("email",
                                foglalasEntity -> foglalasEntity.getUgyfel().getEmail()),
                "nev", "telefon", "email").setHeader("Ügyfél").setWidth("260px");
        //Ha van megjegyzés egy ikon jelezze
        tabla.addColumn(new ComponentRenderer<>(e -> {
            Icon icon = new Icon(VaadinIcon.CLIPBOARD_TEXT);
            if(e.getMegjegyzes().isEmpty()){
                icon.setVisible(false);
            }
            return icon;
        }));
        tabla.addColumn(new NativeButtonRenderer<>("Töröl", this::idopontTorlese));
        tabla.addItemClickListener(e -> {
            if(!"".equals(e.getItem().getMegjegyzes())){
                Label szoveg = new Label(e.getItem().getMegjegyzes());
                Dialog megjegyzes = new Dialog(szoveg);
                megjegyzes.setCloseOnOutsideClick(true);
                megjegyzes.open();
            }
        });
        tabla.setItems(foglalasok);
    }

    private void idopontTorlese(IdopontfoglalasEntity idopontFoglalasEntity){
        alapFoglalasService.torol(idopontFoglalasEntity);

        if(LocalDate.now().isEqual(LocalDate.of(idopontFoglalasEntity.getDatum().getYear(), idopontFoglalasEntity.getDatum().getMonth(), idopontFoglalasEntity.getDatum().getDayOfMonth()))){
            List<IdopontfoglalasEntity> foglalasok = alapFoglalasService.keresesMa();
            sort(foglalasok);
            tablaMa.setItems(foglalasok);
            tablaMa.getDataProvider().refreshAll();
        }else{
            List<IdopontfoglalasEntity> foglalasok = alapFoglalasService.keresesNaptol(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(0,0)));
            sort(foglalasok);
            tabla.setItems(foglalasok);
            tabla.getDataProvider().refreshAll();
        }



    }

}
