package panisz.norbert.simongumis.view;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import panisz.norbert.simongumis.LoggerExample;
import panisz.norbert.simongumis.entities.GumikEntity;
import panisz.norbert.simongumis.repositories.GumiMeretekRepository;
import panisz.norbert.simongumis.repositories.GumikRepository;

import java.util.ArrayList;
import java.util.logging.Logger;


public class GumikView extends HorizontalLayout {

    private final static Logger LOGGER = Logger.getLogger(LoggerExample.class.getName());

    private static GumikRepository alapGumikRepository = null;
    private static GumiMeretekRepository alapGumiMeretekRepository = null;

    private VerticalLayout tartalom = new VerticalLayout();
    private GumiKeresoMenu menu;
    private Grid<GumikEntity> gumik = new Grid<>();

    public GumikView(GumikRepository gumikRepository, GumiMeretekRepository gumiMeretekRepository){
        alapGumikRepository = gumikRepository;
        alapGumiMeretekRepository = gumiMeretekRepository;
        init();
    }

    public void init(){
        menu = new GumiKeresoMenu(alapGumiMeretekRepository);
        tartalom.add(new HorizontalLayout(menu), new HorizontalLayout(gumik));
        menu.getKeres().addClickListener(e -> gumikTablaFeltolt(menu.getKriterium()));

        gumik.addColumn(GumikEntity::getGyarto).setHeader("Gyártó");
        gumik.addColumn(GumikEntity::getMeret).setHeader("Méret");
        gumik.addColumn(GumikEntity::getEvszak).setHeader("Évszak");
        gumik.addColumn(GumikEntity::getAllapot).setHeader("Állapot");
        gumik.addColumn(GumikEntity::getAr).setHeader("Ár");
        gumik.addColumn(GumikEntity::getMennyisegRaktarban).setHeader("Raktáron (db)");
        gumik.setWidth("800px");

        add(tartalom);
    }

    public void gumikTablaFeltolt(GumikEntity szures){
        ArrayList<GumikEntity> osszesGumi = new ArrayList<>(alapGumikRepository.findAll());
        ArrayList<GumikEntity> szurtAdatok = new ArrayList<>(osszesGumi);
        int gumikSzama = osszesGumi.size();
        for(int i=0;i<gumikSzama;i++){
            if(adottSzelessegreSzurtE(osszesGumi.get(i).getMeret().getSzelesseg(), szures.getMeret().getSzelesseg())){
                szurtAdatok.remove(osszesGumi.get(i));
                continue;
            }
            if(adottProfilraSzurtE(osszesGumi.get(i).getMeret().getProfil(), szures.getMeret().getProfil())){
                szurtAdatok.remove(osszesGumi.get(i));
                continue;
            }
            if(adottFelnireSzurtE(osszesGumi.get(i).getMeret().getFelni(), szures.getMeret().getFelni())){
                szurtAdatok.remove(osszesGumi.get(i));
                continue;
            }
            if(adottEvszakraSzurtE(osszesGumi.get(i).getEvszak(),szures.getEvszak())){
                szurtAdatok.remove(osszesGumi.get(i));
                continue;
            }
            if(adottAllapotraSzurtE(osszesGumi.get(i).getAllapot(),szures.getAllapot())){
                szurtAdatok.remove(osszesGumi.get(i));
                continue;
            }
            if(adottGyartoraSzurtE(osszesGumi.get(i).getGyarto(), szures.getGyarto())){
                szurtAdatok.remove(osszesGumi.get(i));
                continue;
            }
            if(adottArraSzurtE(osszesGumi.get(i).getAr())){
                szurtAdatok.remove(osszesGumi.get(i));
                continue;
            }
        }
        gumik.setItems(szurtAdatok);
        gumik.getDataProvider().refreshAll();

    }

    private boolean adottSzelessegreSzurtE(Integer aktualisSzelesseg, Integer szurtSzelesseg){
        return szurtSzelesseg != 0 && !aktualisSzelesseg.equals(szurtSzelesseg);
    }

    private boolean adottProfilraSzurtE(Integer aktualisProfil, Integer szurtProfil){
        return szurtProfil != 0 && !aktualisProfil.equals(szurtProfil);
    }

    private boolean adottFelnireSzurtE(Integer aktualisFelni, Integer szurtFelni){
        return szurtFelni != 0 && !aktualisFelni.equals(szurtFelni);
    }

    private boolean adottEvszakraSzurtE(String aktualisEvszak, String szurtEvszak){
        return szurtEvszak != "" && aktualisEvszak != szurtEvszak;
    }

    private boolean adottAllapotraSzurtE(String aktualisAllapot, String szurtAllapot){
        return szurtAllapot != "" && aktualisAllapot != szurtAllapot;
    }

    private boolean adottGyartoraSzurtE(String aktualisGyarto, String szurtGyarto){
        return szurtGyarto != "" && !aktualisGyarto.equalsIgnoreCase(szurtGyarto);
    }

    private boolean adottArraSzurtE(Integer aktualisAr){
        return aktualisAr < GumiKeresoMenu.getKezdoAr() || (GumiKeresoMenu.getVegAr() != 0 && aktualisAr > GumiKeresoMenu.getVegAr());
    }

}
