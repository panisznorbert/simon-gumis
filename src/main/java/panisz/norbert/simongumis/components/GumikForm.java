package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;
import panisz.norbert.simongumis.LoggerExample;
import panisz.norbert.simongumis.entities.GumikEntity;
import panisz.norbert.simongumis.entities.RendelesEntity;
import panisz.norbert.simongumis.entities.RendelesStatusz;
import panisz.norbert.simongumis.entities.RendelesiEgysegEntity;
import panisz.norbert.simongumis.exceptions.LetezoGumiException;
import panisz.norbert.simongumis.services.GumiMeretekService;
import panisz.norbert.simongumis.services.GumikService;
import panisz.norbert.simongumis.services.RendelesService;
import panisz.norbert.simongumis.spring.SimongumisApplication;
import java.util.ArrayList;
import java.util.logging.Logger;

@UIScope
@Component
public class GumikForm extends VerticalLayout {
    private final static Logger LOGGER = Logger.getLogger(LoggerExample.class.getName());

    private GumikService gumikService;
    private RendelesService rendelesService;

    private GumiKeresoMenu menu;
    private Grid<GumikEntity> gumik = new Grid<>();
    private Dialog darabszamAblak;
    private MenuForm fomenu  = new MenuForm();

    public GumikForm(GumikService gumikService, RendelesService rendelesService, GumiMeretekService gumiMeretekService){
        this.gumikService = gumikService;
        this.rendelesService = rendelesService;
        menu = new GumiKeresoMenu(gumiMeretekService);
        GumiGridBeallitas.gumiGridBeallitas(gumik);
        gumik.addColumn(new NativeButtonRenderer<>("kosárba", this::kosarbahelyezesAblak));
        gumik.setWidth("950px");
        gumik.setHeightByRows(true);
        gumikTablaFeltolt(menu.getKriterium());
        add(fomenu); add( menu, new HorizontalLayout(gumik));
        this.setAlignItems(Alignment.CENTER);
        menu.getKeres().addClickListener(e -> gumikTablaFeltolt(menu.getKriterium()));
        fomenu.getGumik().getStyle().set("color", "blue");
    }


    private void kosarbahelyezesAblak(GumikEntity gumi){
        Label tipus = new Label(gumi.toString() + " típusú gumiból");
        Label hiba = new Label();
        TextField darab = new TextField();
        darab.setPattern("[0-9]*");
        darab.setSuffixComponent(new Span("Db"));
        VerticalLayout leiras = new VerticalLayout(hiba, tipus, darab);
        Button megse  = new Button("Mégse");
        Button ok  = new Button("Ok");
        HorizontalLayout gombok = new HorizontalLayout(ok, megse);
        darabszamAblak = new Dialog(leiras, gombok);
        megse.addClickListener(e -> darabszamAblak.close());
        ok.addClickListener( e -> {
            //Ha nincs megadva is hibás
            if(darab.isEmpty()){
                hiba.setText("Nem adott meg darabszámot");
                darab.setInvalid(true);
                //Ha rossz érték van beírva vagy több mint amennyi van a raktárban akkor hiba jön
            }else if(darab.isInvalid() || Integer.valueOf(darab.getValue())>gumi.getMennyisegRaktarban()){
                hiba.setText("Hibás adat (maximum rendelhető: " + gumi.getMennyisegRaktarban().toString() + " db)");
                darab.setInvalid(true);
            }
            //Ha jó érték van beírva és a hozzáadni kívánt darabszám és a kosárban lévő darabszám összege meghaladja a raktárkészletet akkor hiba jön
            if(!darab.isInvalid() && SimongumisApplication.getRendelesAzon() != null){
                for(RendelesiEgysegEntity rendeles: rendelesService.idKereses(SimongumisApplication.getRendelesAzon()).getRendelesiEgysegek()) {
                    if (rendeles.getGumi().equals(gumi) && rendeles.getMennyiseg()+Integer.valueOf(darab.getValue())>gumi.getMennyisegRaktarban()) {
                        hiba.setText("Hibás adat (maximum rendelhető: " + gumi.getMennyisegRaktarban().toString() + " db, melyből már " + rendeles.getMennyiseg() + " a kosárban van!)");
                        darab.setInvalid(true);
                    }
                }
            }
            if(!darab.isInvalid()){
                kosarbaRak(gumi, Integer.valueOf(darab.getValue()));
                darabszamAblak.close();
            }
        });
        darabszamAblak.open();
        darabszamAblak.setWidth("400px");
    }

    private void kosarbaRak(GumikEntity gumi, Integer darab){
        RendelesiEgysegEntity rendelesiEgyseg = new RendelesiEgysegEntity();
        RendelesEntity rendeles = new RendelesEntity();
        rendeles.setStatusz(RendelesStatusz.KOSARBAN);
        rendelesiEgyseg.setGumi(gumi);
        rendelesiEgyseg.setMennyiseg(darab);
        rendelesiEgyseg.setReszosszeg(gumi.getAr()*darab);
        boolean meglevo = false;
        //Az aktuális rendelés azonosító tárolása, mely később cookie-ban lesz
        if(SimongumisApplication.getRendelesAzon() == null){
            rendeles.setRendelesiEgysegek(new ArrayList<>());

        }else {
            rendeles = rendelesService.idKereses(SimongumisApplication.getRendelesAzon());
        }

        for(RendelesiEgysegEntity rendelesiEgysegEntity : rendeles.getRendelesiEgysegek()){
            //Ellenőrzi hogy az adott gumiból már van-e a kosárban és ha van akkor azt növeli
            if(rendelesiEgysegEntity.getGumi().equals(rendelesiEgyseg.getGumi())){
                rendelesiEgysegEntity.setMennyiseg(rendelesiEgysegEntity.getMennyiseg() + rendelesiEgyseg.getMennyiseg());
                rendelesiEgysegEntity.setReszosszeg(rendelesiEgysegEntity.getReszosszeg() + rendelesiEgyseg.getReszosszeg());
                meglevo = true;
            }
        }

        if(!meglevo){
            rendeles.getRendelesiEgysegek().add(rendelesiEgyseg);
        }

        try{
            SimongumisApplication.setRendelesAzon(rendelesService.ment(rendeles).getId());
            fomenu.getKosar().getStyle().set("color", "red");
            fomenu.getKosar().setIcon(new Icon(VaadinIcon.CART));
        }catch(LetezoGumiException ex){
        Notification hibaAblak = new HibaJelzes(ex.getMessage());
        hibaAblak.open();
    }

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
