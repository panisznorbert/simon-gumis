package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;
import panisz.norbert.simongumis.entities.RendelesEntity;
import panisz.norbert.simongumis.entities.RendelesStatusz;
import panisz.norbert.simongumis.entities.RendelesiEgysegEntity;
import panisz.norbert.simongumis.services.RendelesService;
import java.util.Collections;
import java.util.List;

@UIScope
@Component
public class RendelesekForm extends VerticalLayout {
    private RendelesService rendelesService;

    private HorizontalLayout rendelesekTartalom;

    private TextField nevKereso = new TextField("Név:");
    private Button nevreKeres = new Button("Keres");
    private HorizontalLayout keresoSav = new HorizontalLayout(nevKereso, nevreKeres);
    private VerticalLayout tartalom = new VerticalLayout();

    public RendelesekForm(RendelesService rendelesService){
        this.rendelesService = rendelesService;
        this.setAlignItems(Alignment.CENTER);
        adatBetoltes(rendelesService.rendelesekreKeres(RendelesStatusz.KOSARBAN));
        keresoSav.setAlignItems(Alignment.END);
        nevreKeres.addClickListener(e -> keresesNevre(nevKereso.getValue()));
        rendelesekTartalom = new HorizontalLayout(tartalom);
        add(keresoSav, rendelesekTartalom);

    }


    private void keresesNevre(String nev){
        tartalom.removeAll();
        remove(rendelesekTartalom);
        if(!nev.isEmpty()){
            adatBetoltes(rendelesService.ugyfelNevreKeres(nev));
        }else{
            adatBetoltes(rendelesService.osszes());
        }
        rendelesekTartalom = new HorizontalLayout(tartalom);
        add(rendelesekTartalom);
    }

    private void adatBetoltes(List<RendelesEntity> rendelesek){
        if(!rendelesek.isEmpty()) {
            Collections.sort(rendelesek);
            int darab = rendelesek.size();
            for(int i=0;i<darab-1;i=i+2){
                tartalom.add(new HorizontalLayout(ujRendelesSor(rendelesek.get(i)), ujRendelesSor(rendelesek.get(i+1))));
            }
            if(darab%2 != 0){
                tartalom.add(new HorizontalLayout(ujRendelesSor(rendelesek.get(darab-1))));
            }
        }else if(!nevKereso.getValue().isEmpty()){
            Notification hibaAblak = new Hibajelzes("A megadott feltétel alapján a keresés nem hozott eredményt.");
            hibaAblak.open();
        }
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
        ugyfel.append(" (");
        ugyfel.append(rendelesEntity.getDatum());
        ugyfel.append(")");

        Grid.Column<RendelesiEgysegEntity> oszlop1 = rendelesek
                .addColumn(RendelesiEgysegEntity::getGumi)
                .setHeader("Gumi")
                .setWidth("330px")
                .setFooter("Státusz: " + rendelesEntity.getStatusz().toString());
        Grid.Column<RendelesiEgysegEntity> oszlop2 = rendelesek
                .addColumn(RendelesiEgysegEntity::getMennyiseg)
                .setHeader("Darab")
                .setWidth("85px")
                .setFooter("Végösszeg:");
        Grid.Column<RendelesiEgysegEntity> oszlop3 = rendelesek
                .addColumn(RendelesiEgysegEntity::getReszosszeg)
                .setHeader("Ár")
                .setWidth("85px")
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
                    try{
                        rendelesService.ment(rendelesEntity);
                    }catch(Exception ex){}

                    rendelesek.getFooterRows().get(0).getCell(oszlop1).setComponent(new Label("Státusz: " + rendelesEntity.getStatusz().toString()));


                    modosit.setVisible(false);
                    torol.setVisible(false);
                    break;
                }
                case MEGRENDELVE: {
                    rendelesEntity.setStatusz(RendelesStatusz.ATVETELRE_VAR);
                    try{
                        rendelesService.ment(rendelesEntity);
                    }catch(Exception ex){}
                    rendelesek.getFooterRows().get(0).getCell(oszlop1).setComponent(new Label("Státusz: " + rendelesEntity.getStatusz().toString()));
                    modosit.setText("Átvette");
                    break;
                }
            }
        });
        torol.addClickListener(e -> {
            modosit.setVisible(false);
            rendelesek.getFooterRows().get(0).getCell(oszlop1).setComponent(new Label("Státusz: " + RendelesStatusz.TOROLVE));
            rendelesService.rendelesTrolese(rendelesEntity);
            torol.setVisible(false);
        });

        rendelesek.setHeightByRows(true);
        return rendelesek;
    }

}
