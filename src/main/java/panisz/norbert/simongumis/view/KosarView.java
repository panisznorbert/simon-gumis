package panisz.norbert.simongumis.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Data;
import panisz.norbert.simongumis.LoggerExample;
import panisz.norbert.simongumis.entities.RendelesEntity;
import panisz.norbert.simongumis.entities.RendelesiEgysegEntity;
import panisz.norbert.simongumis.entities.UgyfelEntity;
import panisz.norbert.simongumis.repositories.RendelesRepository;
import panisz.norbert.simongumis.repositories.UgyfelRepository;

import java.util.List;
import java.util.logging.Logger;

import static panisz.norbert.simongumis.entities.RendelesStatusz.MEGRENDELVE;

@Data
public class KosarView extends HorizontalLayout {
    private RendelesEntity alapRendelesEntity = null;

    private VerticalLayout tartalom = new VerticalLayout();
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

        if(rendelesEntity != null && !rendelesEntity.getRendelesiEgysegek().isEmpty()){
            init(rendelesEntity);
        }

    }

    private void init(RendelesEntity rendelesEntity){
        rendelesekTabla.addColumn(RendelesiEgysegEntity::getGumi).setHeader("Gumi").setWidth("300px");
        rendelesekTabla.addColumn(RendelesiEgysegEntity::getMennyiseg).setHeader("Darab");
        rendelesekTabla.addColumn(RendelesiEgysegEntity::getReszosszeg).setHeader("Összeg");
        rendelesekTabla.setWidth("600px");
        vegosszeg.setValue(rendelesEntity.getVegosszeg().toString());
        vegosszeg.setSuffixComponent(new Span("Ft"));
        vegosszeg.setReadOnly(true);
        rendelesekTablaFeltolt(rendelesEntity.getRendelesiEgysegek());
        tartalom.add(rendelesekTabla, vegosszeg, vevoAdatai, gombok);
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
        alapRendelesEntity.setStatusz(MEGRENDELVE.toString());
        ment(alapRendelesEntity, ugyfel);
        MainView.getFomenu().aktualisRendelesekInit();
    }

    private static void ment(RendelesEntity rendelesEntity, UgyfelEntity ugyfel){

        LOGGER.info(rendelesEntity.getUgyfel().toString());
        LOGGER.info(rendelesEntity.getStatusz());
        LOGGER.info(rendelesEntity.getVegosszeg().toString());
        for (RendelesiEgysegEntity rendeles : rendelesEntity.getRendelesiEgysegek()) {
            LOGGER.info(rendeles.getGumi().toString() + " " + rendeles.getMennyiseg() + " " + rendeles.getReszosszeg());
        }

        MainView.getAlapUgyfelRepository().save(ugyfel);
        MainView.getAlapRendelesiEgysegRepository().saveAll(rendelesEntity.getRendelesiEgysegek());
        MainView.getAlapRendelesRepository().save(rendelesEntity);

        for(RendelesEntity rendelesek : MainView.getAlapRendelesRepository().findAll()){
            LOGGER.info(rendelesek.getUgyfel().toString());
            LOGGER.info(rendelesek.getStatusz());
            LOGGER.info(rendelesek.getVegosszeg().toString());
            for (RendelesiEgysegEntity rendeles : rendelesek.getRendelesiEgysegek()) {
                LOGGER.info(rendeles.getGumi().toString() + " " + rendeles.getMennyiseg() + " " + rendeles.getReszosszeg());
            }

        }

    }

}
