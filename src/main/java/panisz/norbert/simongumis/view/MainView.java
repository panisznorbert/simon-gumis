package panisz.norbert.simongumis.view;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import panisz.norbert.simongumis.entities.RendelesiEgysegEntity;
import panisz.norbert.simongumis.repositories.*;

import java.util.List;


@Route
public class MainView extends VerticalLayout{

    private static VerticalLayout layout = new VerticalLayout();

    private static UgyfelRepository alapUgyfelRepository = null;
    private static GumikRepository alapGumikRepository = null;
    private static GumiMeretekRepository alapGumiMeretekRepository = null;
    private static RendelesRepository alapRendelesRepository = null;
    private static RendelesiEgysegRepository alapRendelesiEgysegRepository = null;

    private static GumikKezeleseView gumikKezeleseView = null;
    private static MainViewAlap mainViewAlap = null;
    private static GumikView gumikView = null;
    private static KosarView kosarView = null;

    private static MenuView fomenu = new MenuView();

    private static Component menu = new HorizontalLayout(fomenu);
    private static Component tartalom = new HorizontalLayout();
    private static Component lab = new HorizontalLayout();


    @Autowired
    public MainView(UgyfelRepository ugyfelRepository, GumikRepository gumikRepository, GumiMeretekRepository gumiMeretekRepository, RendelesRepository rendelesRepository, RendelesiEgysegRepository rendelesiEgysegRepository) {
        alapUgyfelRepository = ugyfelRepository;
        alapGumikRepository = gumikRepository;
        alapGumiMeretekRepository = gumiMeretekRepository;
        alapRendelesRepository = rendelesRepository;
        alapRendelesiEgysegRepository = rendelesiEgysegRepository;
        gumikKezeleseView = new GumikKezeleseView(alapGumikRepository, alapGumiMeretekRepository);
        tartalom = gumikKezeleseView;
        layout.add(menu, tartalom, lab);
        add(layout);
    }


    public static void setTartalom(String menupont) {
        if("gumik_kezelese".equals(menupont)){
            if(gumikKezeleseView == null){
                gumikKezeleseView = new GumikKezeleseView(alapGumikRepository, alapGumiMeretekRepository);
            }
            layout.remove(tartalom);
            tartalom = gumikKezeleseView;
            layout.add(tartalom);
        }

        if("alap".equals(menupont)){
            if(mainViewAlap == null){
                mainViewAlap = new MainViewAlap(alapUgyfelRepository);
            }
            layout.remove(tartalom);
            tartalom = mainViewAlap;
            layout.add(tartalom);
        }

        if("gumik".equals(menupont)){
            gumikView = new GumikView(alapGumikRepository, alapGumiMeretekRepository);

            layout.remove(tartalom);
            tartalom = gumikView;
            layout.add(tartalom);
        }

        if("kosar".equals(menupont)){
            kosarView = new KosarView(fomenu.getAktualisRendelesek());

            layout.remove(tartalom);
            tartalom = kosarView;
            layout.add(tartalom);
        }

        if("idopont_foglalas".equals(menupont)){

            layout.remove(tartalom);
            tartalom = new IdopontFoglalasView();
            layout.add(tartalom);
        }

        if("rendelesek".equals(menupont)){

            layout.remove(tartalom);
            tartalom = new RendelesekView(alapRendelesRepository);
            layout.add(tartalom);
        }
    }

    public static void kosarhozAd(RendelesiEgysegEntity rendelesiEgysegEntity){
        Boolean benneVolt = false;
        for(RendelesiEgysegEntity aktualisRendelesiEgyseg:fomenu.getAktualisRendelesek().getRendelesiEgysegek()){
            if(aktualisRendelesiEgyseg.getGumi().equals(rendelesiEgysegEntity.getGumi())){
                fomenu.getAktualisRendelesek().getRendelesiEgysegek().get(fomenu.getAktualisRendelesek().getRendelesiEgysegek().indexOf(aktualisRendelesiEgyseg))
                        .setMennyiseg(
                                fomenu.getAktualisRendelesek().getRendelesiEgysegek().get(fomenu.getAktualisRendelesek().getRendelesiEgysegek().indexOf(aktualisRendelesiEgyseg)).getMennyiseg()+rendelesiEgysegEntity.getMennyiseg()
                        );
                fomenu.getAktualisRendelesek().getRendelesiEgysegek().get(fomenu.getAktualisRendelesek().getRendelesiEgysegek().indexOf(aktualisRendelesiEgyseg))
                        .setReszosszeg(
                                fomenu.getAktualisRendelesek().getRendelesiEgysegek().get(fomenu.getAktualisRendelesek().getRendelesiEgysegek().indexOf(aktualisRendelesiEgyseg)).getReszosszeg()+rendelesiEgysegEntity.getReszosszeg()
                        );
                benneVolt = true;
            }
        }
        if(!benneVolt){
            fomenu.getAktualisRendelesek().getRendelesiEgysegek().add(rendelesiEgysegEntity);

        }
        fomenu.getAktualisRendelesek().setVegosszeg(vegosszegSzamol(fomenu.getAktualisRendelesek().getRendelesiEgysegek()));
    }

    private static Integer vegosszegSzamol(List<RendelesiEgysegEntity> rendelesiEgysegek){
        Integer ossz = 0;
        for (RendelesiEgysegEntity rendelesiEgysegEntity:rendelesiEgysegek) {
            ossz += rendelesiEgysegEntity.getReszosszeg();
        }
        return ossz;
    }

    public static MenuView getFomenu() {
        return fomenu;
    }

    public static RendelesRepository getAlapRendelesRepository() {
        return alapRendelesRepository;
    }

    public static RendelesiEgysegRepository getAlapRendelesiEgysegRepository() {
        return alapRendelesiEgysegRepository;
    }

    public static UgyfelRepository getAlapUgyfelRepository() {
        return alapUgyfelRepository;
    }
}
