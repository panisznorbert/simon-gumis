package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import panisz.norbert.simongumis.LoggerExample;
import panisz.norbert.simongumis.entities.RendelesEntity;
import panisz.norbert.simongumis.entities.RendelesStatusz;
import panisz.norbert.simongumis.entities.RendelesiEgysegEntity;
import panisz.norbert.simongumis.services.implement.RendelesServiceImpl;
import javax.annotation.PostConstruct;
import java.util.List;
import java.util.logging.Logger;

@UIScope
@Component
public class RendelesekForm extends VerticalLayout {
    @Autowired
    private RendelesServiceImpl rendelesService;

    private MenuForm fomenu  = new MenuForm();

    private TextField kereso = new TextField("Név:");
    private Button keres = new Button("Keres");
    private HorizontalLayout keresoSav = new HorizontalLayout(kereso, keres);
    private VerticalLayout tartalom = new VerticalLayout();

    private final static Logger LOGGER = Logger.getLogger(LoggerExample.class.getName());


    @PostConstruct
    private void init(){
        this.setAlignItems(Alignment.CENTER);
        List<RendelesEntity> rendelesEntities = rendelesService.osszes();
        if(!rendelesEntities.isEmpty()) {
            int darab = rendelesEntities.size();
            for(int i=0;i<darab-1;i=i+2){
                tartalom.add(new HorizontalLayout(ujRendelesSor(rendelesEntities.get(i)), ujRendelesSor(rendelesEntities.get(i+1))));
            }
            if(darab%2 != 0){
                tartalom.add(new HorizontalLayout(ujRendelesSor(rendelesEntities.get(darab-1))));
            }
        }
        keresoSav.setAlignItems(Alignment.END);
        add(fomenu, keresoSav, new HorizontalLayout(tartalom));
    }

    private Grid ujRendelesSor(RendelesEntity rendelesEntity){
        Grid<RendelesiEgysegEntity> rendelesek = new Grid<>();
        rendelesek.setWidth("550px");
        Button modosit = new Button("Leegyeztetve");
        Button torol = new Button("Megrendelés törlése");
        HorizontalLayout gombok = new HorizontalLayout(modosit, torol);

        if(RendelesStatusz.ATVETELRE_VAR.equals(rendelesEntity.getStatusz())){
            modosit.setText("Átvette");
        }

        StringBuilder ugyfel = new StringBuilder();
        ugyfel.append("Név: ");
        ugyfel.append(rendelesEntity.getUgyfel().getNev());
        ugyfel.append(", Tel: ");
        ugyfel.append(rendelesEntity.getUgyfel().getTelefon());
        ugyfel.append(", E-mail: ");
        ugyfel.append(rendelesEntity.getUgyfel().getEmail());

        Grid.Column<RendelesiEgysegEntity> oszlop1 = rendelesek
                .addColumn(RendelesiEgysegEntity::getGumi)
                .setHeader("Gumi")
                .setWidth("300px")
                .setFooter("Státusz: " + rendelesEntity.getStatusz().toString());
        Grid.Column<RendelesiEgysegEntity> oszlop2 = rendelesek
                .addColumn(RendelesiEgysegEntity::getMennyiseg)
                .setHeader("Darab")
                .setWidth("100px")
                .setFooter("Végösszeg:");
        Grid.Column<RendelesiEgysegEntity> oszlop3 = rendelesek
                .addColumn(RendelesiEgysegEntity::getReszosszeg)
                .setHeader("Ár")
                .setWidth("100px")
                .setFooter(rendelesEntity.getVegosszeg() + " Ft");


        rendelesek.prependHeaderRow().join(oszlop1, oszlop2, oszlop3).setComponent(new Label(ugyfel.toString()));

        rendelesek.appendFooterRow().join(oszlop1, oszlop2, oszlop3).setComponent(gombok);

        rendelesek.setItems(rendelesEntity.getRendelesiEgysegek());
        rendelesek.getDataProvider().refreshAll();
        if(RendelesStatusz.RENDEZVE.equals(rendelesEntity.getStatusz()) || RendelesStatusz.TOROLVE.equals(rendelesEntity.getStatusz())){
                modosit.setVisible(false);
                torol.setVisible(false);
        }
        modosit.addClickListener(e -> {
            switch (rendelesEntity.getStatusz()){
                case ATVETELRE_VAR:{
                    rendelesEntity.setStatusz(RendelesStatusz.RENDEZVE);
                    rendelesService.ment(rendelesEntity);
                    rendelesek.getFooterRows().get(0).getCell(oszlop1).setComponent(new Label("Státusz: " + rendelesEntity.getStatusz().toString()));


                    modosit.setVisible(false);
                    torol.setVisible(false);
                    break;
                }
                case MEGRENDELVE: {
                    rendelesEntity.setStatusz(RendelesStatusz.ATVETELRE_VAR);
                    rendelesService.ment(rendelesEntity);
                    rendelesek.getFooterRows().get(0).getCell(oszlop1).setComponent(new Label("Státusz: " + rendelesEntity.getStatusz().toString()));
                    modosit.setText("Átvette");
                    break;
                }
            }
        });
        torol.addClickListener(e -> {
            rendelesEntity.setStatusz(RendelesStatusz.TOROLVE);
            modosit.setVisible(false);
            rendelesek.getFooterRows().get(0).getCell(oszlop1).setComponent(new Label("Státusz: " + rendelesEntity.getStatusz().toString()));
            rendelesService.rendelesTrolese(rendelesEntity);
            torol.setVisible(false);
        });

        rendelesek.setHeightByRows(true);
        return rendelesek;
    }

}
