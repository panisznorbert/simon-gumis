package panisz.norbert.simongumis.view;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import panisz.norbert.simongumis.entities.GumikEntity;
import panisz.norbert.simongumis.repositories.GumiMeretekRepository;
import panisz.norbert.simongumis.repositories.GumikRepository;

public class GumikView extends HorizontalLayout {

    private static GumikRepository alapGumikRepository = null;
    private static GumiMeretekRepository alapGumiMeretekRepository = null;
    private HorizontalLayout tartalom = new HorizontalLayout();

    private Grid<GumikEntity> grid = new Grid<>();

    public GumikView(GumikRepository gumikRepository, GumiMeretekRepository gumiMeretekRepository){
        alapGumikRepository = gumikRepository;
        alapGumiMeretekRepository = gumiMeretekRepository;
        init();
    }

    public void init(){
        tartalom = new GumiKeresoMenu(alapGumiMeretekRepository);
        add(tartalom);
    }
}
