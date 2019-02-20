package panisz.norbert.simongumis.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Data;
import org.springframework.transaction.annotation.Transactional;
import panisz.norbert.simongumis.LoggerExample;
import panisz.norbert.simongumis.entities.RendelesEntity;
import panisz.norbert.simongumis.entities.RendelesStatusz;
import panisz.norbert.simongumis.entities.RendelesiEgysegEntity;
import panisz.norbert.simongumis.entities.UgyfelEntity;
import panisz.norbert.simongumis.repositories.RendelesRepository;
import panisz.norbert.simongumis.repositories.UgyfelRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static panisz.norbert.simongumis.entities.RendelesStatusz.MEGRENDELVE;

@Data
public class KosarView extends HorizontalLayout {
    private RendelesEntity alapRendelesEntity = null;

    private HorizontalLayout tartalom = new HorizontalLayout();
    private Grid<RendelesiEgysegEntity> rendelesekTabla = new Grid<>();

    private TextField nev = new TextField("Név:");
    private TextField email = new TextField("E-mail:");
    private TextField telefon = new TextField("Telefon:");
    private VerticalLayout vevoAdatai = new VerticalLayout(nev, email, telefon);
    private Button tovabb = new Button("Megrendelés");
    private HorizontalLayout gombok = new HorizontalLayout(tovabb);
    private TextField vegosszeg = new TextField("Végösszeg:");

    private final static Logger LOGGER = Logger.getLogger(LoggerExample.class.getName());

    public KosarView(RendelesEntity rendelesEntity){
        alapRendelesEntity = rendelesEntity;

        if(alapRendelesEntity != null && !alapRendelesEntity.getRendelesiEgysegek().isEmpty()){
            init();
        }
    }

    private void init(){
        rendelesekTabla.addColumn(RendelesiEgysegEntity::getGumi).setHeader("Gumi").setWidth("300px");
        rendelesekTabla.addColumn(RendelesiEgysegEntity::getMennyiseg).setHeader("Darab");
        rendelesekTabla.addColumn(RendelesiEgysegEntity::getReszosszeg).setHeader("Összeg");
        rendelesekTabla.setWidth("600px");
        vegosszeg.setValue(alapRendelesEntity.getVegosszeg().toString());
        vegosszeg.setSuffixComponent(new Span("Ft"));
        vegosszeg.setReadOnly(true);
        rendelesekTablaFeltolt(alapRendelesEntity.getRendelesiEgysegek());
        tartalom.add(new VerticalLayout(rendelesekTabla, vegosszeg), new VerticalLayout(vevoAdatai, gombok));
        add(tartalom);
        tovabb.addClickListener(e -> megrendeles());
    }

    private void rendelesekTablaFeltolt(List<RendelesiEgysegEntity> rendelesiEgysegek){
        rendelesekTabla.setItems(rendelesiEgysegek);
        rendelesekTabla.getDataProvider().refreshAll();
    }

    private void megrendeles(){
        UgyfelEntity ugyfel = new UgyfelEntity();
        ugyfel.setNev(nev.getValue());
        ugyfel.setEmail(email.getValue());
        ugyfel.setTelefon(telefon.getValue());
        alapRendelesEntity.setUgyfel(ugyfel);
        alapRendelesEntity.setStatusz(RendelesStatusz.MEGRENDELVE);
        ment(alapRendelesEntity);
        MainView.getFomenu().aktualisRendelesekInit();
        MainView.setTartalom("gumik");
    }

    private static void ment(RendelesEntity rendelesEntity){
        MainView.getAlapRendelesRepository().save(rendelesEntity);
    }

}
