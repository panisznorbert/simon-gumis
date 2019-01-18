package panisz.norbert.simongumis.view;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import panisz.norbert.simongumis.repositories.GumikRepository;
import panisz.norbert.simongumis.repositories.UgyfelRepository;


@Route

public class MainView extends VerticalLayout{

    private static VerticalLayout layout = new VerticalLayout();

    private static UgyfelRepository alapUgyfelRepository = null;
    private static GumikRepository alapGumikRepository = null;

    private static GumikView gumikView = null;
    private static MainViewAlap mainViewAlap = null;

    private static Component menu = new HorizontalLayout();
    private static Component tartalom = new HorizontalLayout();
    private static Component lab = new HorizontalLayout();


    @Autowired
    public MainView(UgyfelRepository ugyfelRepository, GumikRepository gumikRepository) {
        menu = new MenuView();
        alapUgyfelRepository = ugyfelRepository;
        alapGumikRepository = gumikRepository;
        gumikView = new GumikView(alapGumikRepository);
        tartalom = gumikView;
        layout.add(menu, tartalom, lab);

        add(layout);
    }


    public static void setTartalom(String menupont) {


        if(menupont=="gumik"){
            if(gumikView == null){
                gumikView = new GumikView(alapGumikRepository);
            }
            layout.remove(tartalom);
            tartalom = gumikView;
            layout.add(tartalom);
        }

        if(menupont=="alap"){
            if(mainViewAlap == null){
                mainViewAlap = new MainViewAlap(alapUgyfelRepository);
            }
            layout.remove(tartalom);
            tartalom = mainViewAlap;
            layout.add(tartalom);
        }

    }


}
