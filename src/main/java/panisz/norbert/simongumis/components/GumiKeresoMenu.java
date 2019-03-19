package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import panisz.norbert.simongumis.LoggerExample;
import panisz.norbert.simongumis.entities.GumiMeretekEntity;
import panisz.norbert.simongumis.entities.GumikEntity;
import panisz.norbert.simongumis.services.implement.GumiMeretekServiceImpl;
import javax.annotation.PostConstruct;
import java.util.*;
import java.util.logging.Logger;

@UIScope
@Component
public class GumiKeresoMenu extends HorizontalLayout {
    @Autowired
    private GumiMeretekServiceImpl gumiMeretekService;

    private GumikEntity kriterium = new GumikEntity();
    private static Integer kezdoAr;
    private static Integer vegAr;

    private ComboBox<Integer> meret1 = new ComboBox("Méret-szélesség");
    private ComboBox<Integer> meret2 = new ComboBox("Méret-profil arány");
    private ComboBox<Integer> meret3 = new ComboBox("Méret-felni átmérő");
    private HorizontalLayout meret1cb = new HorizontalLayout(meret1);
    private HorizontalLayout meret2cb = new HorizontalLayout(meret2);
    private HorizontalLayout meret3cb = new HorizontalLayout(meret3);

    private ComboBox evszak = new ComboBox("Évszak", "Téli", "Nyári", "Négyévszakos");
    private ComboBox allapot = new ComboBox("Állapot", "Új","Használt");

    private TextField gyarto = new TextField("Gyártó");
    private TextField artol = new TextField("Ár -tól");
    private TextField arig = new TextField("Ár -ig");

    private Button egyeb = new Button("+ feltételek");
    private Button alaphelyzet = new Button("Alaphelyzet");
    private Button keres = new Button("Keresés");

    private HorizontalLayout menu1 = new HorizontalLayout();
    private HorizontalLayout menu2 = new HorizontalLayout();
    private VerticalLayout menu = new VerticalLayout();

    private final static Logger LOGGER = Logger.getLogger(LoggerExample.class.getName());

    public GumiKeresoMenu(){
        egyeb.addClickListener(e -> tovabbiFeltetelek());
        alaphelyzet.addClickListener(e -> init());
        keres.addClickListener(e -> adatokBeallitasa());
    }

    @PostConstruct
    private void init(){
        meretFeltolto(gumiMeretekService.osszes(), 0, 0, 0);
        kriterium.setMeret(new GumiMeretekEntity());
        setMeret(0,0,0);
        kriterium.setGyarto("");
        kriterium.setAllapot("");
        kriterium.setEvszak("");
        kriterium.setMennyisegRaktarban(0);
        kriterium.setAr(0);
        evszak.clear();
        allapot.clear();
        gyarto.clear();
        artol.clear();
        arig.clear();
        menu1.removeAll();
        menu1.add(meret1cb, meret2cb, meret3cb, evszak, allapot);
        menu2.removeAll();
        menu2.add(egyeb, alaphelyzet, keres);
        menu2.setAlignItems(Alignment.BASELINE);
        menu.setHeight("200px");
        menu.removeAll();
        menu.add(menu1, menu2);
        add(menu);
        adatokBeallitasa();
    }

    private void adatokBeallitasa(){
        if(gyarto.isEmpty()){
            kriterium.setGyarto("");
        }else{
            kriterium.setGyarto(gyarto.getValue());
        }

        if(artol.isEmpty()){
            kezdoAr=0;
        }else{
            kezdoAr=Integer.valueOf(artol.getValue());
        }

        if(arig.isEmpty()){
            vegAr=0;
        }else{
            vegAr=Integer.valueOf(arig.getValue());
        }

        if(evszak.isEmpty()){
            kriterium.setEvszak("");
        }else{
            kriterium.setEvszak(evszak.getValue().toString());
        }

        if(allapot.isEmpty()){
            kriterium.setAllapot("");
        }else{
            kriterium.setAllapot(allapot.getValue().toString());
        }
    }


    private void szelessegKivalasztas(){
        Integer beallito = 111;
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
                setMeret(0, 0, 0);
                break;
            }
            case 1:{
                meretFeltolto(gumiMeretekService.felnireKeres(meret3.getValue()), 0, 0, 1);
                setMeret(0, 0, meret3.getValue());
                break;
            }
            case 10:{
                meretFeltolto(gumiMeretekService.profilraKeres(meret2.getValue()),0 ,1 ,0);
                setMeret(0, meret2.getValue(), 0);
                break;
            }
            case 11:{
                meretFeltolto(gumiMeretekService.profilraEsFelnireKeres(meret2.getValue(), meret3.getValue()),0,1,1);
                setMeret(0, meret2.getValue(), meret3.getValue());
                break;
            }
            case 100:{
                meretFeltolto(gumiMeretekService.szelessegreKeres(meret1.getValue()), 1, 0, 0);
                setMeret(meret1.getValue(), 0, 0);
                break;
            }
            case 101:{
                meretFeltolto(gumiMeretekService.szelessegreEsFelnireKeres(meret1.getValue(), meret3.getValue()), 1, 0, 1);
                setMeret(meret1.getValue(), 0, meret3.getValue());
                break;
            }
            case 110:{
                meretFeltolto(gumiMeretekService.szelessegreEsProfilraKeres(meret1.getValue(), meret2.getValue()), 1, 1, 0);
                setMeret(meret1.getValue(), meret2.getValue(), 0);
                break;
            }
            case 111:{
                setMeret(meret1.getValue(), meret2.getValue(), meret3.getValue());
                break;
            }
        }
    }

    private void setMeret(Integer a, Integer b, Integer c){
        kriterium.getMeret().setSzelesseg(a);
        kriterium.getMeret().setProfil(b);
        kriterium.getMeret().setFelni(c);
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

        meret1cb.remove(meret1);meret1 = new ComboBox("Méret-szélesség");meret1.setItems(meretSet1);meret1cb.add(meret1);
        meret2cb.remove(meret2);meret2 = new ComboBox("Méret-profil arány");meret2.setItems(meretSet2);meret2cb.add(meret2);
        meret3cb.remove(meret3);meret3 = new ComboBox("Méret-felni átmérő");meret3.setItems(meretSet3);meret3cb.add(meret3);

        if(a==1){meret1.setValue(gumiMeretekEntities.get(0).getSzelesseg());}
        if(b==1){meret2.setValue(gumiMeretekEntities.get(0).getProfil());}
        if(c==1){meret3.setValue(gumiMeretekEntities.get(0).getFelni());}

        meret1.addValueChangeListener(e -> szelessegKivalasztas());
        meret2.addValueChangeListener(e -> szelessegKivalasztas());
        meret3.addValueChangeListener(e -> szelessegKivalasztas());
    }

    private void tovabbiFeltetelek(){
        menu2.removeAll();
        menu2.add(gyarto, artol, arig, alaphelyzet, keres);
        menu.setHeight("300px");
    }


    public Button getKeres() {
        return keres;
    }

    public GumikEntity getKriterium() {
        return kriterium;
    }

    public static Integer getKezdoAr() { return kezdoAr; }

    public static Integer getVegAr() {
        return vegAr;
    }
}
