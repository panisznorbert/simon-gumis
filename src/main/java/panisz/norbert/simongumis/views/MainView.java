package panisz.norbert.simongumis.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import panisz.norbert.simongumis.components.*;
import panisz.norbert.simongumis.entities.RendelesiEgysegEntity;
import javax.annotation.PostConstruct;
import java.util.List;

@Route
public class MainView extends VerticalLayout{
    private static MenuForm fomenu;

    private static VerticalLayout tartalom;

    private static VerticalLayout layout;

    @PostConstruct
    public void init() {
        fomenu = new MenuForm();
        tartalom = new VerticalLayout();
        layout = new VerticalLayout(fomenu, tartalom);

        add(layout);
    }


}
