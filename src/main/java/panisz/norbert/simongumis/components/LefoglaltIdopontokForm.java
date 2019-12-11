package panisz.norbert.simongumis.components;

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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

import static java.util.Collections.sort;

@UIScope
@Component
public class LefoglaltIdopontokForm extends VerticalLayout {

    private IdopontfoglalasServie foglalasService;

    private Grid<IdopontfoglalasEntity> tabla = new Grid<>();

    private HorizontalLayout keresoSor = new HorizontalLayout();

    private HorizontalLayout foglalasok = new HorizontalLayout();

    public LefoglaltIdopontokForm(IdopontfoglalasServie foglalasService){
        this.foglalasService = foglalasService;
        this.setAlignItems(Alignment.CENTER);
        tabla.setWidth("650px");
        tabla.setHeightByRows(true);
        tabla.setVerticalScrollingEnabled(false);
        List<IdopontfoglalasEntity> tobbiFoglalasok = foglalasService.keresesNaptol(LocalDateTime.now());
        if(tobbiFoglalasok != null && !tobbiFoglalasok.isEmpty()){
            tabla = tablafeltolto(tobbiFoglalasok);
            foglalasok.add(tabla);
        }

        add(keresoSor, foglalasok);

    }


    private Grid<IdopontfoglalasEntity> tablafeltolto(List<IdopontfoglalasEntity> foglalasok){
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

        return tabla;
    }

    private void idopontTorlese(IdopontfoglalasEntity idopontFoglalasEntity){
        foglalasService.torol(idopontFoglalasEntity);
        List<IdopontfoglalasEntity> foglalasok = foglalasService.keresesNaptol(LocalDateTime.now());
        sort(foglalasok);
        tabla.setItems(foglalasok);
        tabla.getDataProvider().refreshAll();
    }

}
