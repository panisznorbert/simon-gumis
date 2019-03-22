package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import panisz.norbert.simongumis.LoggerExample;
import panisz.norbert.simongumis.entities.*;
import panisz.norbert.simongumis.services.implement.RendelesServiceImpl;
import panisz.norbert.simongumis.spring.SpringApplication;
import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

@UIScope
@Component
public class KosarForm extends VerticalLayout {
    @Autowired
    private RendelesServiceImpl rendelesService;

    private MenuForm fomenu  = new MenuForm();

    private RendelesEntity rendelesEntity;
    private VerticalLayout tartalom = new VerticalLayout();
    private Grid<RendelesiEgysegEntity> rendelesekTabla = new Grid<>();

    private UgyfelMezok vevoAdatai = new UgyfelMezok();
    private Button megrendeles = new Button("Megrendelés");
    private HorizontalLayout gombok = new HorizontalLayout(megrendeles);
    private TextField vegosszeg = new TextField("Végösszeg:");

    private final static Logger LOGGER = Logger.getLogger(LoggerExample.class.getName());

    @PostConstruct
    private void init(){
        this.setAlignItems(Alignment.CENTER);
        add(fomenu);
        rendelesekTabla.setHeightByRows(true);
        rendelesekTabla.addColumn(RendelesiEgysegEntity::getGumi).setHeader("Gumi").setWidth("300px");
        rendelesekTabla.addColumn(RendelesiEgysegEntity::getMennyiseg).setHeader("Darab");
        rendelesekTabla.addColumn(RendelesiEgysegEntity::getReszosszeg).setHeader("Összeg");
        rendelesekTabla.setWidth("600px");
        vegosszeg.setSuffixComponent(new Span("Ft"));
        vegosszeg.setReadOnly(true);

        if(SpringApplication.getRendelesAzon() != null) {
            rendelesEntity = rendelesService.idKereses(SpringApplication.getRendelesAzon());
            rendelesekTablaFeltolt(rendelesEntity.getRendelesiEgysegek());
            rendelesEntity.setVegosszeg(rendelesVegosszeg());
            vegosszeg.setValue(rendelesEntity.getVegosszeg().toString());
            tartalom.add(new VerticalLayout(rendelesekTabla, vegosszeg), new VerticalLayout(vevoAdatai, gombok));
            add(new HorizontalLayout(tartalom));
            megrendeles.addClickListener(e -> megrendeles());
        }
    }

    private Integer rendelesVegosszeg(){
        Integer osszeg = 0;
        for(RendelesiEgysegEntity rendeles : rendelesEntity.getRendelesiEgysegek()){
            osszeg += rendeles.getReszosszeg();
        }

        return osszeg;
    }

    private void rendelesekTablaFeltolt(List<RendelesiEgysegEntity> rendelesiEgysegek){
        rendelesekTabla.setItems(rendelesiEgysegek);
        rendelesekTabla.getDataProvider().refreshAll();
    }

    private void megrendeles(){
        UgyfelEntity ugyfel = new UgyfelEntity();
        ugyfel.setNev(vevoAdatai.getNev().getValue());
        ugyfel.setEmail(vevoAdatai.getEmail().getValue());
        ugyfel.setTelefon(vevoAdatai.getTelefon().getValue());
        rendelesEntity.setUgyfel(ugyfel);
        rendelesEntity.setStatusz(RendelesStatusz.MEGRENDELVE);
        rendelesEntity.setDatum(LocalDate.now());
        String hiba = rendelesService.mentKosarbol(rendelesEntity);
        if(hiba != null){
            Notification hibaAblak = new HibaJelzes(hiba);
            hibaAblak.open();
        }else{
            SpringApplication.setRendelesAzon(null);
            UI.getCurrent().navigate("gumik");
        }

    }

}
