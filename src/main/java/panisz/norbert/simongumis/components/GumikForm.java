package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import panisz.norbert.simongumis.LoggerExample;
import panisz.norbert.simongumis.entities.GumikEntity;
import panisz.norbert.simongumis.entities.RendelesiEgysegEntity;
import panisz.norbert.simongumis.repositories.GumikRepository;
import panisz.norbert.simongumis.views.MainView;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.logging.Logger;

@UIScope
@Component
public class GumikForm extends VerticalLayout {
    private final static Logger LOGGER = Logger.getLogger(LoggerExample.class.getName());

    @Autowired
    private GumikRepository gumikRepository;

    private MenuForm fomenu  = new MenuForm();
    @Autowired
    private GumiKeresoMenu menu = new GumiKeresoMenu();
    private Grid<GumikEntity> gumik = new Grid<>();

    private Dialog darabszamAblak;


    @PostConstruct
    public void init(){
        gumik.addColumn(GumikEntity::getGyarto).setHeader("Gyártó");
        gumik.addColumn(GumikEntity::getMeret).setHeader("Méret");
        gumik.addColumn(GumikEntity::getEvszak).setHeader("Évszak");
        gumik.addColumn(GumikEntity::getAllapot).setHeader("Állapot");
        gumik.addColumn(GumikEntity::getAr).setHeader("Ár");
        gumik.addColumn(GumikEntity::getMennyisegRaktarban).setHeader("Raktáron (db)");
        gumik.addColumn(new NativeButtonRenderer<>("kosárba", this::kosarbahelyezesAblak));
        gumik.setWidth("950px");
        gumikTablaFeltolt(menu.getKriterium());
        add(fomenu, menu, gumik);
        menu.getKeres().addClickListener(e -> gumikTablaFeltolt(menu.getKriterium()));
    }


    private void kosarbahelyezesAblak(GumikEntity gumi){
        Label tipus = new Label(gumi.toString() + " típusú gumiból");
        Label hiba = new Label();
        TextField darab = new TextField();
        darab.setPattern("[0-9]*");
        darab.setSuffixComponent(new Span("Db"));
        VerticalLayout leiras = new VerticalLayout(tipus, hiba, darab);
        Button megse  = new Button("Mégse");
        Button ok  = new Button("Ok");
        HorizontalLayout gombok = new HorizontalLayout(ok, megse);
        darabszamAblak = new Dialog(leiras, gombok);
        megse.addClickListener(e -> darabszamAblak.close());
        ok.addClickListener( e -> {
            //Ha rossz érték van beírva vagy több mint amennyi van a raktárban akkor hiba jön
            if(darab.isInvalid() || Integer.valueOf(darab.getValue())>gumi.getMennyisegRaktarban()){
                hiba.setText("Hibás adat (maximum rendelhető: " + gumi.getMennyisegRaktarban().toString() + " db)");
                darab.setInvalid(true);
            }
            //Ha jó érték van beírva és a hozzáadni kívánt darabszám és a kosárban lévő darabszám összege meghaladja a raktárkészletet akkor hiba jön
            if(!darab.isInvalid() && !MainView.getFomenu().getAktualisRendeles().getRendelesiEgysegek().isEmpty()){
                for(RendelesiEgysegEntity rendeles:MainView.getFomenu().getAktualisRendeles().getRendelesiEgysegek()) {
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
        RendelesiEgysegEntity rendeles = new RendelesiEgysegEntity();
        rendeles.setGumi(gumi);
        rendeles.setMennyiseg(darab);
        rendeles.setReszosszeg(gumi.getAr()*darab);
        MainView.kosarhozAd(rendeles);
    }

    public void gumikTablaFeltolt(GumikEntity szures){
        ArrayList<GumikEntity> osszesGumi = new ArrayList<>(gumikRepository.findAll());
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
