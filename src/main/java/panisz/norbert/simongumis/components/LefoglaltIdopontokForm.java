package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.IconRenderer;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import panisz.norbert.simongumis.entities.FoglalasEntity;
import panisz.norbert.simongumis.services.implement.FoglalasServiceImpl;

import javax.annotation.PostConstruct;
import java.util.List;

@UIScope
@Component
public class LefoglaltIdopontokForm extends VerticalLayout {

    @Autowired
    private FoglalasServiceImpl foglalasService;

    private MenuForm fomenu  = new MenuForm();

    private VerticalLayout mai = new VerticalLayout();
    private VerticalLayout tobbiFoglalas = new VerticalLayout();

    private Grid<FoglalasEntity> maiLista = new Grid<>();
    private Grid<FoglalasEntity> tobbiFoglalasLista = new Grid<>();

    @PostConstruct
    private void init(){
        add(fomenu);
    }

    private void foglalasokBetoltese(){

    }

    private Grid<FoglalasEntity> tablafeltolto(List<FoglalasEntity> foglalasok){
        Grid<FoglalasEntity> tabla = new Grid<>();

        tabla.addColumn(FoglalasEntity::getDatum).setHeader("Időpont");
        tabla.addColumn(FoglalasEntity::getUgyfel).setHeader("Ügyfél");
        //tabla.addColumn(new IconRenderer<>(VaadinIcon.FILE_TEXT_O)).setHeader("Időpont");

        return tabla;
    }

    private void megjegyzesNezet(){

    }
}
