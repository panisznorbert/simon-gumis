package panisz.norbert.simongumis.view;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import panisz.norbert.simongumis.entities.GumikEntity;

public class GumikView extends HorizontalLayout {

    private HorizontalLayout tartalom = new HorizontalLayout();

    private Grid<GumikEntity> grid = new Grid<>();

    public GumikView(){
        init();
    }

    private void init(){
        tartalom = new GumiKeresoMenu();
        add(tartalom);
    }
}
