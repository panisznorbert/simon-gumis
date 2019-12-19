package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;
import panisz.norbert.simongumis.entities.*;
import panisz.norbert.simongumis.services.GumiMeretekService;
import panisz.norbert.simongumis.services.GumikService;
import panisz.norbert.simongumis.services.RendelesService;
import java.util.ArrayList;

@StyleSheet("style.css")
@UIScope
@Component
public class GumikForm extends VerticalLayout {
    private GumikService gumikService;
    private GumiKeresoMenu menu;
    private VerticalLayout gumilap;

    public GumikForm(GumikService gumikService, RendelesService rendelesService, GumiMeretekService gumiMeretekService, FoMenu foMenu){
        this.gumikService = gumikService;
        menu = new GumiKeresoMenu(gumiMeretekService);

        gumilap = new GumikLap(gumikService.osszes(), "ugyfel", rendelesService, foMenu.getKosar());
        add( menu, gumilap);
        this.setAlignItems(Alignment.CENTER);
        menu.getKeres().addClickListener(e -> gumikTablaFeltolt(menu.getKriterium()));
    }


    private void gumikTablaFeltolt(GumikEntity szures){
        ArrayList<GumikEntity> osszesGumi = new ArrayList<>(gumikService.osszes());
        ArrayList<GumikEntity> szurtAdatok = new ArrayList<>(osszesGumi);
        for (GumikEntity gumikEntity : osszesGumi) {
            if (adottSzelessegreSzurtE(gumikEntity.getMeret().getSzelesseg(), szures.getMeret().getSzelesseg())) {
                szurtAdatok.remove(gumikEntity);
                continue;
            }
            if (adottProfilraSzurtE(gumikEntity.getMeret().getProfil(), szures.getMeret().getProfil())) {
                szurtAdatok.remove(gumikEntity);
                continue;
            }
            if (adottFelnireSzurtE(gumikEntity.getMeret().getFelni(), szures.getMeret().getFelni())) {
                szurtAdatok.remove(gumikEntity);
                continue;
            }
            if (adottEvszakraSzurtE(gumikEntity.getEvszak(), szures.getEvszak())) {
                szurtAdatok.remove(gumikEntity);
                continue;
            }
            if (adottAllapotraSzurtE(gumikEntity.getAllapot(), szures.getAllapot())) {
                szurtAdatok.remove(gumikEntity);
                continue;
            }
            if (adottGyartoraSzurtE(gumikEntity.getGyarto(), szures.getGyarto())) {
                szurtAdatok.remove(gumikEntity);
                continue;
            }
            if (adottArraSzurtE(gumikEntity.getAr())) {
                szurtAdatok.remove(gumikEntity);
            }
            //amiből nulla darab van azt ne jelenítse meg
            if (gumikEntity.getMennyisegRaktarban().equals(0)){
                szurtAdatok.remove(gumikEntity);
            }
        }

        this.remove(gumilap);
        gumilap = new GumikLap(szurtAdatok, "ugyfel");
        this.add(gumilap);
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
        return !szurtEvszak.equals("") && !aktualisEvszak.equals(szurtEvszak);
    }

    private boolean adottAllapotraSzurtE(String aktualisAllapot, String szurtAllapot){
        return !szurtAllapot.equals("") && !aktualisAllapot.equals(szurtAllapot);
    }

    private boolean adottGyartoraSzurtE(String aktualisGyarto, String szurtGyarto){
        return !szurtGyarto.equals("") && !aktualisGyarto.equalsIgnoreCase(szurtGyarto);
    }

    private boolean adottArraSzurtE(Integer aktualisAr){
        return aktualisAr < GumiKeresoMenu.getKezdoAr() || (GumiKeresoMenu.getVegAr() != 0 && aktualisAr > GumiKeresoMenu.getVegAr());
    }

}
