package panisz.norbert.simongumis.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import panisz.norbert.simongumis.LoggerExample;
import panisz.norbert.simongumis.entities.RendelesEntity;
import panisz.norbert.simongumis.entities.RendelesiEgysegEntity;
import panisz.norbert.simongumis.repositories.RendelesRepository;

import java.util.List;
import java.util.logging.Logger;

public class RendelesekView extends VerticalLayout {
    private RendelesRepository alapRendelesRepository = null;

    private TextField kereso = new TextField("Név:");
    private Button keres = new Button("Keres");
    private HorizontalLayout keresoSav = new HorizontalLayout(kereso, keres);
    private VerticalLayout tartalom = new VerticalLayout();

    private final static Logger LOGGER = Logger.getLogger(LoggerExample.class.getName());

    public RendelesekView(RendelesRepository rendelesRepository){
        LOGGER.info(rendelesRepository.findAll().get(0).getUgyfel().getNev());
        alapRendelesRepository = rendelesRepository;
        init(alapRendelesRepository.findAll());
    }

    private void init(List<RendelesEntity> rendelesEntities){
        if(rendelesEntities != null && !rendelesEntities.isEmpty()) {
            for (RendelesEntity rendeles : rendelesEntities) {
                ujRendelesSor(rendeles);
            }
        }
        keresoSav.setAlignItems(Alignment.END);
        add(keresoSav, tartalom);
    }

    private void ujRendelesSor(RendelesEntity rendelesEntity){
        TextField nev = new TextField("Név:");
        TextField email = new TextField("E-mail:");
        TextField telefon = new TextField("Telefon:");
        HorizontalLayout uygfelLeiras = new HorizontalLayout(nev, email, telefon);
        Grid<RendelesiEgysegEntity> rendelesek = new Grid<>();
        VerticalLayout ugyfelRendeles = new VerticalLayout(rendelesek);
        TextField ar = new TextField("Ár");
        TextField statusz = new TextField("Státusz:");
        HorizontalLayout labLec = new HorizontalLayout(ar, statusz);
        nev.setValue(rendelesEntity.getUgyfel().getNev());
        email.setValue(rendelesEntity.getUgyfel().getEmail());
        telefon.setValue(rendelesEntity.getUgyfel().getTelefon());
        ar.setValue(rendelesEntity.getVegosszeg().toString());
        statusz.setValue(rendelesEntity.getStatusz());
        rendelesek.addColumn(RendelesiEgysegEntity::getGumi).setHeader("Gumi");
        rendelesek.addColumn(RendelesiEgysegEntity::getMennyiseg).setHeader("Darab");
        rendelesek.addColumn(RendelesiEgysegEntity::getReszosszeg).setHeader("Ár");
        rendelesek.setItems(rendelesEntity.getRendelesiEgysegek());
        rendelesek.getDataProvider().refreshAll();
        tartalom.add(new VerticalLayout(uygfelLeiras, ugyfelRendeles, labLec));
    }
}
