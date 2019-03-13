package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import panisz.norbert.simongumis.LoggerExample;
import panisz.norbert.simongumis.entities.GumikEntity;
import panisz.norbert.simongumis.entities.RendelesEntity;
import panisz.norbert.simongumis.entities.RendelesStatusz;
import panisz.norbert.simongumis.entities.RendelesiEgysegEntity;
import panisz.norbert.simongumis.services.implement.GumikServiceImpl;
import panisz.norbert.simongumis.services.implement.RendelesServiceImpl;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.logging.Logger;

@UIScope
@Component
public class RendelesekForm extends VerticalLayout {
    @Autowired
    private RendelesServiceImpl rendelesService;
    @Autowired
    private GumikServiceImpl gumikService;

    private MenuForm fomenu  = new MenuForm();

    private TextField kereso = new TextField("Név:");
    private Button keres = new Button("Keres");
    private HorizontalLayout keresoSav = new HorizontalLayout(kereso, keres);
    private VerticalLayout tartalom = new VerticalLayout();

    private final static Logger LOGGER = Logger.getLogger(LoggerExample.class.getName());


    @PostConstruct
    private void init(){
        List<RendelesEntity> rendelesEntities = rendelesService.osszes();
        if(!rendelesEntities.isEmpty()) {
            for (RendelesEntity rendeles : rendelesEntities) {
                ujRendelesSor(rendeles);
            }
        }
        keresoSav.setAlignItems(Alignment.END);
        add(fomenu, keresoSav, tartalom);
    }

    private void ujRendelesSor(RendelesEntity rendelesEntity){
        TextField nev = new TextField("Név:");
        TextField email = new TextField("E-mail:");
        TextField telefon = new TextField("Telefon:");
        HorizontalLayout uygfelLeiras = new HorizontalLayout(nev, email, telefon);
        Grid<RendelesiEgysegEntity> rendelesek = new Grid<>();
        rendelesek.setWidth("550px");
        VerticalLayout ugyfelRendeles = new VerticalLayout(rendelesek);
        TextField ar = new TextField("Ár");
        TextField statusz = new TextField("Státusz:");
        Button modosit = new Button("Leegyeztetve");
        if(RendelesStatusz.ATVETELRE_VAR.equals(rendelesEntity.getStatusz())){
            modosit.setText("Átvette");
        }
        Button torol = new Button("Megrendelés törlése");
        HorizontalLayout labLec = new HorizontalLayout(ar, statusz, modosit, torol);
        labLec.setAlignItems(Alignment.BASELINE);
        nev.setValue(rendelesEntity.getUgyfel().getNev());
        email.setValue(rendelesEntity.getUgyfel().getEmail());
        telefon.setValue(rendelesEntity.getUgyfel().getTelefon());
        ar.setValue(rendelesEntity.getVegosszeg().toString());
        statusz.setValue(rendelesEntity.getStatusz().toString());
        statusz.setEnabled(false);
        rendelesek.addColumn(RendelesiEgysegEntity::getGumi).setHeader("Gumi").setWidth("300px");
        rendelesek.addColumn(RendelesiEgysegEntity::getMennyiseg).setHeader("Darab").setWidth("100px");
        rendelesek.addColumn(RendelesiEgysegEntity::getReszosszeg).setHeader("Ár").setWidth("100px");
        rendelesek.setItems(rendelesEntity.getRendelesiEgysegek());
        rendelesek.getDataProvider().refreshAll();
        if(RendelesStatusz.RENDEZVE.equals(rendelesEntity.getStatusz()) || RendelesStatusz.TOROLVE.equals(rendelesEntity.getStatusz())){
                modosit.setVisible(false);
                torol.setVisible(false);
        }
        modosit.addClickListener(e -> {
            switch (statusz.getValue()){
                case "ATVETELRE_VAR":{
                    rendelesEntity.setStatusz(RendelesStatusz.RENDEZVE);
                    rendelesService.ment(rendelesEntity);
                    statusz.setValue(RendelesStatusz.RENDEZVE.toString());
                    modosit.setVisible(false);
                    break;
                }
                case "MEGRENDELVE": {
                    rendelesEntity.setStatusz(RendelesStatusz.ATVETELRE_VAR);
                    rendelesService.ment(rendelesEntity);
                    statusz.setValue(RendelesStatusz.ATVETELRE_VAR.toString());
                    modosit.setText("Átvette");
                    break;
                }
            }
        });
        torol.addClickListener(e -> {
            //ha már átvételre várt és mégis törölve lesz akkor a lefoglalt darabszám visszakerül a raktárkészletbe
            if(rendelesEntity.getStatusz().equals(RendelesStatusz.ATVETELRE_VAR)){
                for(RendelesiEgysegEntity rendelesiEgysegEntity : rendelesEntity.getRendelesiEgysegek()){
                    GumikEntity gumikEntity = gumikService.idKereses(rendelesiEgysegEntity.getGumi().getId());
                    gumikEntity.setMennyisegRaktarban(gumikEntity.getMennyisegRaktarban()+rendelesiEgysegEntity.getMennyiseg());
                    gumikService.ment(gumikEntity);
                }
            }
            modosit.setVisible(false);
            statusz.setValue(RendelesStatusz.TOROLVE.toString());
            rendelesEntity.setStatusz(RendelesStatusz.TOROLVE);
            rendelesService.ment(rendelesEntity);
            torol.setVisible(false);
        });

        tartalom.add(new VerticalLayout(uygfelLeiras, ugyfelRendeles, labLec));
    }


}
