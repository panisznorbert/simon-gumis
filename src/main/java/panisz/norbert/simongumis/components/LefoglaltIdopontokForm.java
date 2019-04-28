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
import panisz.norbert.simongumis.entities.IdopontFoglalasEntity;
import panisz.norbert.simongumis.services.IdopontFoglalasService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

import static java.util.Collections.sort;

@UIScope
@Component
public class LefoglaltIdopontokForm extends VerticalLayout {

    private IdopontFoglalasService foglalasService;

    private FoMenu fomenu  = new FoMenu();

    private Grid<IdopontFoglalasEntity> tabla = new Grid<>();

    private HorizontalLayout keresoSor = new HorizontalLayout();

    private HorizontalLayout foglalasok = new HorizontalLayout();

    public LefoglaltIdopontokForm(IdopontFoglalasService foglalasService){
        this.foglalasService = foglalasService;
        this.setAlignItems(Alignment.CENTER);
        tabla.setWidth("650px");
        tabla.setHeightByRows(true);
        tabla.setVerticalScrollingEnabled(false);
        add(fomenu);
        List<IdopontFoglalasEntity> tobbiFoglalasok = foglalasService.keresesNaptol(LocalDateTime.now());
        if(tobbiFoglalasok != null && !tobbiFoglalasok.isEmpty()){
            tabla = tablafeltolto(tobbiFoglalasok);
            foglalasok.add(tabla);
        }

        add(keresoSor, foglalasok);
        fomenu.getLefoglaltIdopontok().getStyle().set("color", "blue");
    }


    private Grid<IdopontFoglalasEntity> tablafeltolto(List<IdopontFoglalasEntity> foglalasok){
        sort(foglalasok);
        tabla.addColumn(new LocalDateTimeRenderer<>(IdopontFoglalasEntity::getDatum, DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT)), "datum").setHeader("Időpont").setWidth("160px");
        tabla.addColumn(TemplateRenderer.<IdopontFoglalasEntity> of(
                "<div><small>Név: [[item.nev]]<br>Tel: [[item.telefon]]<br>E-mail: [[item.email]]</small></div>")
                        .withProperty("nev",
                                foglalasEntity -> foglalasEntity.getUgyfel().getNev())
                        .withProperty("telefon",
                                foglalasEntity -> foglalasEntity.getUgyfel().getTelefon())
                        .withProperty("email",
                                foglalasEntity -> foglalasEntity.getUgyfel().getEmail()),
                "nev", "telefon", "email").setHeader("Ügyfél").setWidth("280px");
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

    private void idopontTorlese(IdopontFoglalasEntity idopontFoglalasEntity){
        foglalasService.torol(idopontFoglalasEntity);
        List<IdopontFoglalasEntity> foglalasok = foglalasService.keresesNaptol(LocalDateTime.now());
        sort(foglalasok);
        tabla.setItems(foglalasok);
        tabla.getDataProvider().refreshAll();
    }

}
