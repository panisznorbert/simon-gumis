package panisz.norbert.simongumis.view;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import panisz.norbert.simongumis.repositories.GumiMeretekRepository;
import panisz.norbert.simongumis.repositories.GumikRepository;
import panisz.norbert.simongumis.repositories.UgyfelRepository;


@Route

public class MainView extends VerticalLayout{

    private static VerticalLayout layout = new VerticalLayout();

    private static UgyfelRepository alapUgyfelRepository = null;
    private static GumikRepository alapGumikRepository = null;
    private static GumiMeretekRepository alapGumiMeretekRepository = null;

    private static GumikKezeleseView gumikKezeleseView = null;
    private static MainViewAlap mainViewAlap = null;
    private static GumikView gumikView = null;

    private static Component menu = new HorizontalLayout();
    private static Component tartalom = new HorizontalLayout();
    private static Component lab = new HorizontalLayout();


    @Autowired
    public MainView(UgyfelRepository ugyfelRepository, GumikRepository gumikRepository, GumiMeretekRepository gumiMeretekRepository) {
        menu = new MenuView();
        alapUgyfelRepository = ugyfelRepository;
        alapGumikRepository = gumikRepository;
        alapGumiMeretekRepository = gumiMeretekRepository;
        gumikKezeleseView = new GumikKezeleseView(alapGumikRepository, alapGumiMeretekRepository);
        tartalom = gumikKezeleseView;
        layout.add(menu, tartalom, lab);
        add(layout);
    }


    public static void setTartalom(String menupont) {
        if(menupont=="gumik_kezelese"){
            if(gumikKezeleseView == null){
                gumikKezeleseView = new GumikKezeleseView(alapGumikRepository, alapGumiMeretekRepository);
            }
            layout.remove(tartalom);
            tartalom = gumikKezeleseView;
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

        if(menupont=="gumik"){
            gumikView = new GumikView(alapGumikRepository, alapGumiMeretekRepository);

            layout.remove(tartalom);
            tartalom = gumikView;
            layout.add(tartalom);
        }
    }


}
