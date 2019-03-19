package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import panisz.norbert.simongumis.entities.FoglalasEntity;
import panisz.norbert.simongumis.services.implement.FoglalasServiceImpl;
import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;

import static java.util.Collections.sort;

@UIScope
@Component
public class LefoglaltIdopontokForm extends VerticalLayout {

    @Autowired
    private FoglalasServiceImpl foglalasService;

    private MenuForm fomenu  = new MenuForm();

    private VerticalLayout mai = new VerticalLayout();
    private VerticalLayout tobbiFoglalas = new VerticalLayout();
    private VaadinIcon Icon;

    @PostConstruct
    private void init(){
        add(fomenu);
        List<FoglalasEntity> tobbiFoglalasok = foglalasService.keresesNaptol(LocalDate.now());
        if(tobbiFoglalasok != null){

            Grid<FoglalasEntity> foglalasLista = tablafeltolto(tobbiFoglalasok);
            tobbiFoglalas.add(foglalasLista);
        }

        add(mai, tobbiFoglalas);
    }


    private Grid<FoglalasEntity> tablafeltolto(List<FoglalasEntity> foglalasok){
        Grid<FoglalasEntity> tabla = new Grid<>();
        sort(foglalasok);
        tabla.addColumn(FoglalasEntity::getDatum, "Dátum").setHeader("Időpont");
        tabla.addColumn(TemplateRenderer.<FoglalasEntity> of(
                "<div>[[item.nev]]<br><small>[[item.telefon]]<br>[[item.email]]</small></div>")

                        .withProperty("nev",
                                foglalasEntity -> foglalasEntity.getUgyfel().getNev())
                        .withProperty("telefon",
                                foglalasEntity -> foglalasEntity.getUgyfel().getTelefon())
                        .withProperty("email",
                                foglalasEntity -> foglalasEntity.getUgyfel().getEmail()),
                "nev", "telefon", "email").setHeader("Ügyfél");
        //Ha van megjegyzés egy ikon jelezze
        tabla.setItems(foglalasok);

        return tabla;
    }

    private void megjegyzesNezet(){

    }
}
