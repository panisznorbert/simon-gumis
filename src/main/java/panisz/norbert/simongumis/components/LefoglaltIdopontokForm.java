package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.*;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import panisz.norbert.simongumis.entities.IdopontFoglalasEntity;
import panisz.norbert.simongumis.services.implement.IdopontIdopontFoglalasServiceImpl;
import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

import static java.util.Collections.sort;

@UIScope
@Component
public class LefoglaltIdopontokForm extends VerticalLayout {

    @Autowired
    private IdopontIdopontFoglalasServiceImpl foglalasService;

    private MenuForm fomenu  = new MenuForm();

    private Grid<IdopontFoglalasEntity> tabla = new Grid<>();

    private HorizontalLayout keresoSor = new HorizontalLayout();

    private HorizontalLayout foglalasok = new HorizontalLayout();

    @PostConstruct
    private void init(){
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
        tabla.setItemDetailsRenderer(TemplateRenderer.<IdopontFoglalasEntity> of(
                "<div><b>[[item.megjegyzes]]</b></div>")
                .withProperty("megjegyzes", IdopontFoglalasEntity::getMegjegyzes)
                .withEventHandler("handleClick", foglalasEntity -> tabla.getDataProvider().refreshItem(foglalasEntity)));

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
