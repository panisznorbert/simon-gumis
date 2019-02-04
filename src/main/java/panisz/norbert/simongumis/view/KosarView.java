package panisz.norbert.simongumis.view;

import com.vaadin.flow.component.button.Button;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Data;
import panisz.norbert.simongumis.entities.RendelesEntity;
import panisz.norbert.simongumis.entities.RendelesiEgysegEntity;

import java.util.List;

@Data
public class KosarView extends HorizontalLayout {
    private static RendelesEntity alapRendelesEntity = null;

    private VerticalLayout tartalom = new VerticalLayout();
    private Grid<RendelesiEgysegEntity> rendelesekTabla = new Grid<>();
    private HorizontalLayout gombok = new HorizontalLayout();
    private TextField vegosszeg = new TextField("Végösszeg:");
    private Button tovabb = new Button("Tovább");

    public KosarView(RendelesEntity rendelesEntity){
        alapRendelesEntity = rendelesEntity;
        init();
        rendelesekTablaFeltolt(alapRendelesEntity.getRendelesiEgysegek());
    }

    private void init(){
        rendelesekTabla.addColumn(RendelesiEgysegEntity::getGumi).setHeader("Gumi").setWidth("300px");
        rendelesekTabla.addColumn(RendelesiEgysegEntity::getMennyiseg).setHeader("Darab");
        rendelesekTabla.addColumn(RendelesiEgysegEntity::getReszosszeg).setHeader("Összeg");
        rendelesekTabla.setWidth("600px");
        vegosszeg.setValue(vegosszegSzamol(alapRendelesEntity.getRendelesiEgysegek()).toString());
        vegosszeg.setSuffixComponent(new Span("Ft"));
        vegosszeg.setReadOnly(true);
        tartalom.add(rendelesekTabla, vegosszeg, gombok);
        gombok.add(tovabb);
        add(tartalom);
    }

    private void rendelesekTablaFeltolt(List<RendelesiEgysegEntity> rendelesiEgysegek){
        rendelesekTabla.setItems(rendelesiEgysegek);
        rendelesekTabla.getDataProvider().refreshAll();
            }

    private Integer vegosszegSzamol(List<RendelesiEgysegEntity> rendelesiEgysegek){
        Integer ossz = 0;
        for (RendelesiEgysegEntity rendelesiEgysegEntity:rendelesiEgysegek) {
            ossz += rendelesiEgysegEntity.getReszosszeg();
        }
        return ossz;
    }

    public static RendelesEntity getAlapRendelesEntity() {
        return alapRendelesEntity;
    }
}
