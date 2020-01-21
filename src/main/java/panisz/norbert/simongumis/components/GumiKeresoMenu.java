package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;
import panisz.norbert.simongumis.entities.GumiMeretekEntity;
import panisz.norbert.simongumis.entities.GumikEntity;
import panisz.norbert.simongumis.services.GumiMeretekService;
import panisz.norbert.simongumis.services.GumikService;

import java.util.*;

@StyleSheet("almenu.css")
@UIScope
@Component
public class GumiKeresoMenu extends HorizontalLayout {

    private GumiMeretekService gumiMeretekService;


    private ComboBox<Integer> meret1 = new ComboBox();
    private ComboBox<Integer> meret2 = new ComboBox();
    private ComboBox<Integer> meret3 = new ComboBox();
    private HorizontalLayout meret1cb = new HorizontalLayout(meret1);
    private HorizontalLayout meret2cb = new HorizontalLayout(meret2);
    private HorizontalLayout meret3cb = new HorizontalLayout(meret3);

    private ComboBox<String> evszak = new ComboBox("", "Téli", "Nyári", "Négyévszakos");
    private ComboBox<String> allapot = new ComboBox("", "Új","Használt");

    private TextField gyarto = new TextField();
    private TextField artol = new TextField();
    private TextField arig = new TextField();

    private Button egyeb = new Button("+ Opciók");
    private Button alaphelyzet = new Button("Alaphelyzet");
    private Button keres = new Button("Keresés");

    private HorizontalLayout menu1 = new HorizontalLayout();
    private HorizontalLayout menu2 = new HorizontalLayout();
    private VerticalLayout menu = new VerticalLayout();

    public GumiKeresoMenu(GumiMeretekService gumiMeretekService){
        this.gumiMeretekService = gumiMeretekService;
        egyeb.addClickListener(e -> tovabbiFeltetelek());
        alaphelyzet.addClickListener(e -> init());
        init();
        meret1.setId("combobox-meret");
        meret2.setId("combobox-meret");
        meret3.setId("combobox-meret");
        evszak.setId("combobox-meret");
        allapot.setId("combobox-meret");
        gyarto.setId("kereso-input");
        artol.setId("kereso-input");
        arig.setId("kereso-input");
    }


    private void init(){
        meretFeltolto(gumiMeretekService.osszes(), 0, 0, 0);
        evszak.clear();
        evszak.setPlaceholder("Évszak");
        allapot.clear();
        allapot.setPlaceholder("Állapot");
        gyarto.clear();
        gyarto.setPlaceholder("Gyártó");
        artol.clear();
        artol.setPlaceholder("Ár-tól");
        arig.clear();
        arig.setPlaceholder("Ár-ig");
        menu1.removeAll();
        menu1.add(meret1cb, meret2cb, meret3cb, evszak, allapot);
        menu2.removeAll();
        menu2.add(egyeb, alaphelyzet, keres);
        menu2.setAlignItems(Alignment.BASELINE);
        menu2.setId("menu2");
        menu.removeAll();
        menu.add(menu1, menu2);
        add(menu);
    }

    private void meretKivalasztas(){
        int beallito = 111;
        if(meret1.isEmpty()){
            beallito = beallito - 100;
        }
        if(meret2.isEmpty()) {
            beallito = beallito - 10;
        }
        if(meret3.isEmpty()) {
            beallito = beallito - 1;
        }

        switch(beallito){
            case 0:{
                meretFeltolto(gumiMeretekService.osszes(), 0, 0, 0);
                break;
            }
            case 1:{
                meretFeltolto(gumiMeretekService.felnireKeresMenuhoz(meret3.getValue()), 0, 0, 1);
                break;
            }
            case 10:{
                meretFeltolto(gumiMeretekService.profilraKeresMenuhoz(meret2.getValue()),0 ,1 ,0);
                break;
            }
            case 11:{
                meretFeltolto(gumiMeretekService.profilraEsFelnireKeresMenuhoz(meret2.getValue(), meret3.getValue()),0,1,1);
                break;
            }
            case 100:{
                meretFeltolto(gumiMeretekService.szelessegreKeresMenuhoz(meret1.getValue()), 1, 0, 0);
                break;
            }
            case 101:{
                meretFeltolto(gumiMeretekService.szelessegreEsFelnireKeresMenuhoz(meret1.getValue(), meret3.getValue()), 1, 0, 1);
                break;
            }
            case 110:{
                meretFeltolto(gumiMeretekService.szelessegreEsProfilraKeresMenuhoz(meret1.getValue(), meret2.getValue()), 1, 1, 0);
                break;
            }
            case 111:{
                break;
            }
        }
    }


    private void meretFeltolto(List<GumiMeretekEntity> gumiMeretekEntities, int a, int b, int c){
        SortedSet<Integer> meretSet1 = new TreeSet<>();
        SortedSet<Integer> meretSet2 = new TreeSet<>();
        SortedSet<Integer> meretSet3 = new TreeSet<>();
        for (GumiMeretekEntity gumiMeretekEntity : gumiMeretekEntities) {
            meretSet1.add(gumiMeretekEntity.getSzelesseg());
            meretSet2.add(gumiMeretekEntity.getProfil());
            meretSet3.add(gumiMeretekEntity.getFelni());
        }

        meret1cb.remove(meret1);meret1 = new ComboBox();meret1.setItems(meretSet1);meret1cb.add(meret1);
        meret2cb.remove(meret2);meret2 = new ComboBox();meret2.setItems(meretSet2);meret2cb.add(meret2);
        meret3cb.remove(meret3);meret3 = new ComboBox();meret3.setItems(meretSet3);meret3cb.add(meret3);
        meret1.setPlaceholder("Méret-szélesség");
        meret2.setPlaceholder("Méret-profil");
        meret3.setPlaceholder("Méret-felni");
        meret1.setId("combobox-meret");
        meret2.setId("combobox-meret");
        meret3.setId("combobox-meret");

        if(a==1){meret1.setValue(gumiMeretekEntities.get(0).getSzelesseg());}
        if(b==1){meret2.setValue(gumiMeretekEntities.get(0).getProfil());}
        if(c==1){meret3.setValue(gumiMeretekEntities.get(0).getFelni());}

        meret1.addValueChangeListener(e -> meretKivalasztas());
        meret2.addValueChangeListener(e -> meretKivalasztas());
        meret3.addValueChangeListener(e -> meretKivalasztas());
    }

    private void tovabbiFeltetelek(){
        menu2.removeAll();
        menu2.add(gyarto, artol, arig, alaphelyzet, keres);
        menu.setHeight("200px");
    }

    List<GumikEntity> szurtGumik(GumikService gumikService){
        ArrayList<GumikEntity> osszesGumi = new ArrayList<>(gumikService.osszes());
        ArrayList<GumikEntity> szurtAdatok = new ArrayList<>(osszesGumi);
        for (GumikEntity gumikEntity : osszesGumi) {
            if (adottSzelessegreSzurtE(gumikEntity.getMeret().getSzelesseg(), meret1.isEmpty() ? 0 : meret1.getValue())) {
                szurtAdatok.remove(gumikEntity);
                continue;
            }
            if (adottProfilraSzurtE(gumikEntity.getMeret().getProfil(), meret2.isEmpty() ? 0 : meret2.getValue())) {
                szurtAdatok.remove(gumikEntity);
                continue;
            }
            if (adottFelnireSzurtE(gumikEntity.getMeret().getFelni(), meret3.isEmpty() ? 0 : meret3.getValue())) {
                szurtAdatok.remove(gumikEntity);
                continue;
            }
            if (adottEvszakraSzurtE(gumikEntity.getEvszak(), evszak.isEmpty() ? "" : evszak.getValue())) {
                szurtAdatok.remove(gumikEntity);
                continue;
            }
            if (adottAllapotraSzurtE(gumikEntity.getAllapot(), allapot.isEmpty() ? "" : allapot.getValue())) {
                szurtAdatok.remove(gumikEntity);
                continue;
            }
            if (adottGyartoraSzurtE(gumikEntity.getGyarto(), gyarto.getValue())) {
                szurtAdatok.remove(gumikEntity);
                continue;
            }
            try{
                if (adottArraSzurtE(gumikEntity.getAr())) {
                    szurtAdatok.remove(gumikEntity);
                }
            }catch(NumberFormatException ex){
                Hibajelzes hibajelzes = new Hibajelzes("Hibásan adta meg az árat!");
                hibajelzes.open();
                return null;
            }

            //amiből nulla darab van azt ne jelenítse meg
            if (gumikEntity.getMennyisegRaktarban().equals(0)){
                szurtAdatok.remove(gumikEntity);
            }
        }

        return szurtAdatok;
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
        return aktualisAr < (artol.isEmpty() ? 0 : Integer.parseInt(artol.getValue())) || ((arig.isEmpty() ? 0 : Integer.parseInt(arig.getValue())) != 0 && aktualisAr > (arig.isEmpty() ? 0 : Integer.parseInt(arig.getValue())));
    }


    Button getKeres() {
        return keres;
    }

    Button getEgyeb() {
        return egyeb;
    }

    Button getAlaphelyzet() {return alaphelyzet; }

}
