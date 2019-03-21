package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import panisz.norbert.simongumis.entities.FoglalasEntity;
import panisz.norbert.simongumis.services.implement.FoglalasServiceImpl;
import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

import static java.util.Collections.sort;

@UIScope
@Component
public class LefoglaltIdopontokForm extends VerticalLayout {

    @Autowired
    private FoglalasServiceImpl foglalasService;

    private MenuForm fomenu  = new MenuForm();

    private Grid<FoglalasEntity> tabla = new Grid<>();

    private HorizontalLayout keresoSor = new HorizontalLayout();

    private HorizontalLayout foglalasok = new HorizontalLayout();

    @PostConstruct
    private void init(){
        this.setAlignItems(Alignment.CENTER);
        tabla.setWidth("600px");
        tabla.setHeightByRows(true);
        add(fomenu);
        List<FoglalasEntity> tobbiFoglalasok = foglalasService.keresesNaptol(LocalDate.now());
        if(tobbiFoglalasok != null && !tobbiFoglalasok.isEmpty()){

            tabla = tablafeltolto(tobbiFoglalasok);
            foglalasok.add(tabla);
        }

        add(keresoSor, foglalasok);
    }


    private Grid<FoglalasEntity> tablafeltolto(List<FoglalasEntity> foglalasok){

        sort(foglalasok);
        tabla.addColumn(new LocalDateTimeRenderer<>(FoglalasEntity::getDatum, DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT)), "datum").setHeader("Időpont").setWidth("150px");
        tabla.addColumn(TemplateRenderer.<FoglalasEntity> of(
                "<div><small>Név: [[item.nev]]<br>Tel: [[item.telefon]]<br>E-mail: [[item.email]]</small></div>")

                        .withProperty("nev",
                                foglalasEntity -> foglalasEntity.getUgyfel().getNev())
                        .withProperty("telefon",
                                foglalasEntity -> foglalasEntity.getUgyfel().getTelefon())
                        .withProperty("email",
                                foglalasEntity -> foglalasEntity.getUgyfel().getEmail()),
                "nev", "telefon", "email").setHeader("Ügyfél").setWidth("250px");
        tabla.addColumn(new NativeButtonRenderer<>("Töröl", this::idopontTorlese));
        //Ha van megjegyzés egy ikon jelezze
        tabla.setItems(foglalasok);

        return tabla;
    }

    private void idopontTorlese(FoglalasEntity foglalasEntity){
        foglalasService.torol(foglalasEntity);
        tabla.setItems(foglalasService.osszes());
        tabla.getDataProvider().refreshAll();
    }

    private void megjegyzesNezet(){

    }
}
