package panisz.norbert.simongumis.view;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import panisz.norbert.simongumis.repositories.UgyfelRepository;


@Route

public class MainView extends VerticalLayout{

    private static VerticalLayout layout = new VerticalLayout();

    private static Component menu = new MenuView();
    private static Component tartalom;
    private static Component lab = new HorizontalLayout();

    private static UgyfelRepository alapUgyfelRepository = null;


    @Autowired
    public MainView(UgyfelRepository ugyfelRepository) {
        alapUgyfelRepository=ugyfelRepository;
        tartalom = new MainViewAlap(ugyfelRepository);
        layout.add(menu, tartalom, lab);

        add(layout);
    }


    public static void setTartalom(String menupont) {

        switch (menupont){
            case "gumik": tartalom=new GumikView();
            case "alap" : tartalom=new MainViewAlap(alapUgyfelRepository);
        }


    }


}
