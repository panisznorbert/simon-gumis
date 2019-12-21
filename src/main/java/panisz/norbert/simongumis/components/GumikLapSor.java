package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import panisz.norbert.simongumis.entities.*;
import panisz.norbert.simongumis.services.AdminService;
import panisz.norbert.simongumis.services.GumikService;
import panisz.norbert.simongumis.services.RendelesService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;

public class GumikLapSor extends HorizontalLayout {

    private AppLayoutMenuItem kosar;

    private VerticalLayout kepHelye = new VerticalLayout();
    private VerticalLayout leirasHelye1 = new VerticalLayout();
    private VerticalLayout leirasHelye2 = new VerticalLayout();
    private VerticalLayout megrendelesHelye = new VerticalLayout();
    private VerticalLayout modositasHelye = new VerticalLayout();


    GumikLapSor(GumikEntity gumi, GumikService gumikService, RendelesService rendelesService, AppLayoutMenuItem kosar, AdminService adminService){
        this.kosar = kosar;
        //Gumiról kép
        Button gumiKep = kepBetolt(gumi.getKep());
        gumiKep.setId("kep-button");
        gumiKep.addClickListener(e -> sorkivalasztas(gumi.getKep()));

        kepHelye.add(gumiKep);
        kepHelye.setSizeUndefined();
        kepHelye.setId("kepHelye");

        //Leírás első része
        Label gyarto = new Label(gumi.getGyarto());
        gyarto.setId("gyarto-label");
        Label meret = new Label(gumi.getMeret().toString());
        meret.setId("meret-label");
        Label evszak = new Label(gumi.getEvszak());
        evszak.setId("evszak-label");
        Label allapot = new Label(gumi.getAllapot());
        allapot.setId("allapot-label");

        leirasHelye1.add(gyarto, meret, evszak, allapot);
        leirasHelye1.setSizeUndefined();
        leirasHelye1.setId("leirasHelye1");

        //Leíras második része
        Label keszlet = new Label("Készleten: " + gumi.getMennyisegRaktarban().toString() + " db");
        keszlet.setId("keszlet-label");
        Label ar = new Label(gumi.getAr().toString() + " Ft/db");
        ar.setId("ar-label");

        leirasHelye2.add(keszlet, ar);
        leirasHelye2.setSizeUndefined();
        leirasHelye2.setId("leirasHelye2");

        //Megrendelés mezők
        Label darabszamHiba = new Label("maximum " + gumi.getMennyisegRaktarban() + " db");
        darabszamHiba.setId("darabszamHiba-label");
        darabszamHiba.setVisible(false);
        TextField darab = new TextField();
        darab.setPattern("[0-9]*");
        darab.setId("darab-mezo");
        darab.setValue("0");
        Button kosarba = new Button("kosárba");
        kosarba.setId("kosarba");
        kosarba.addClickListener(e -> kosarbahelyezes(gumi, darab, rendelesService));
        Button csokkent = new Button("-");
        csokkent.setId("csokkent");
        csokkent.addClickListener(e -> rendelesCsokkentese(darab, darabszamHiba, kosarba));
        Button novel = new Button("+");
        novel.setId("novel");
        novel.addClickListener(e -> rendelesNovelese(darab, gumi.getMennyisegRaktarban(), darabszamHiba, kosarba));
        HorizontalLayout darabMegrendeles = new HorizontalLayout(csokkent, darab, novel);

        megrendelesHelye.add(darabMegrendeles, darabszamHiba, kosarba);
        megrendelesHelye.setSizeUndefined();
        megrendelesHelye.setId("megrendelesHelye");

        //Sorok felépítése
        this.add(kepHelye, leirasHelye1, leirasHelye2, megrendelesHelye);
        this.addClassName("gumik-sor");

        //if (adminService.sessionreKeres(UI.getCurrent().getSession().getSession().getId()) != null){
        Icon torol = new Icon(VaadinIcon.TRASH);
        torol.setId("torol-ikon");
        torol.addClickListener(e -> torles(gumi, gumikService));
        Icon szerkeszt = new Icon(VaadinIcon.EDIT);
        szerkeszt.setId("szerkeszt-ikon");
        szerkeszt.addClickListener(e -> szerkeszt(gumi));
        modositasHelye.add(torol, szerkeszt);
        modositasHelye.setSizeUndefined();
        modositasHelye.setId("sorvege");
        this.add(modositasHelye);
        //}else{
        //   megrendelesHelye.setId("sorvege");
        //}
    }

    private void torles(GumikEntity gumi, GumikService gumikService){
        gumikService.torol(gumi);
        this.setVisible(false);
    }

    private void szerkeszt(GumikEntity gumi){

    }

    private void rendelesCsokkentese(TextField mezo, Label hiba, Button gomb){
        if(hiba.isVisible()){
            hiba.setVisible(false);
            gomb.getStyle().set("margin-top", "30px");
        }
        if(!mezo.getValue().equals("0")){
            int adat = Integer.parseInt(mezo.getValue())-1;
            mezo.setValue(Integer.toString(adat));
        }
    }

    private void rendelesNovelese(TextField mezo, int max, Label hiba, Button gomb){
        hiba.setVisible(false);
        if(!mezo.getValue().equals(Integer.toString(max))){
            int adat = Integer.parseInt(mezo.getValue())+1;
            mezo.setValue(Integer.toString(adat));
        }else{
            hiba.setVisible(true);
            gomb.getStyle().set("margin-top", "10px");
        }
    }

    private static Button kepBetolt(byte[] gumikEntity){
        Button kep = new Button();
        if(gumikEntity != null){
            StreamResource streamResource = new StreamResource("isr", new InputStreamFactory() {
                @Override
                public InputStream createInputStream() {
                    return new ByteArrayInputStream(gumikEntity);
                }
            });
            kep.setIcon(new Image(streamResource, ""));
        }else{
            kep.setIcon(new Icon(VaadinIcon.BAN));
            kep.setEnabled(false);
        }
        return kep;
    }

    private void sorkivalasztas(byte[] gumikEntity){
        Dialog kepNagyit = new Dialog();
        Image kep;
        StreamResource streamResource = new StreamResource("isr", new InputStreamFactory() {
            @Override
            public InputStream createInputStream() {
                return new ByteArrayInputStream(gumikEntity);
            }
        });
        kep = new Image(streamResource, "");
        kep.setWidth("400px");
        kepNagyit.setCloseOnOutsideClick(true);
        kepNagyit.add(kep);
        kepNagyit.open();
    }

    private void kosarbahelyezes(GumikEntity gumi, TextField darab, RendelesService rendelesService){
        int darabszam = Integer.parseInt(darab.getValue());
        Notification hiba = new Hibajelzes("");
        if(darabszam == 0){
            return;
        }

        if(rendelesService.sessionreKeres(UI.getCurrent().getSession().getSession().getId()) != null) {
            for(RendelesiEgysegEntity rendeles: rendelesService.sessionreKeres(UI.getCurrent().getSession().getSession().getId()).getRendelesiEgysegek()) {
                if (rendeles.getGumi().getGumiId().equals(gumi.getId()) && rendeles.getMennyiseg() + darabszam>gumi.getMennyisegRaktarban()) {
                    hiba.setText("Hibás adat (maximum rendelhető: " + gumi.getMennyisegRaktarban().toString() + " db, melyből már " + rendeles.getMennyiseg() + " a kosárban van!)");
                    hiba.open();
                    return;
                }
            }
        }
        darab.setValue("0");
        kosarbaRak(gumi, darabszam, rendelesService);
    }

    private void kosarbaRak(GumikEntity gumi, Integer darab, RendelesService rendelesService){
        RendelesiEgysegEntity rendelesiEgyseg = new RendelesiEgysegEntity();
        MegrendeltGumikEntity megrendeltGumikEntity = new MegrendeltGumikEntity();
        RendelesEntity rendeles = new RendelesEntity();
        rendeles.setStatusz(RendelesStatusz.KOSARBAN);
        rendeles.setSession(UI.getCurrent().getSession().getSession().getId());
        rendeles.setDatum(LocalDate.now());

        rendelesiEgyseg.setGumi(megrendeltGumikEntity.beallit(gumi));
        rendelesiEgyseg.setMennyiseg(darab);
        rendelesiEgyseg.setReszosszeg(gumi.getAr()*darab);
        boolean meglevo = false;
        //Az aktuális rendelés azonosító tárolása, mely később cookie-ban lesz
        if(rendelesService.sessionreKeres(UI.getCurrent().getSession().getSession().getId()) == null){
            rendeles.setRendelesiEgysegek(new ArrayList<>());

        }else {
            rendeles = rendelesService.sessionreKeres(UI.getCurrent().getSession().getSession().getId());
        }

        for(RendelesiEgysegEntity rendelesiEgysegEntity : rendeles.getRendelesiEgysegek()){
            //Ellenőrzi hogy az adott gumiból már van-e a kosárban és ha van akkor azt növeli
            if(rendelesiEgysegEntity.getGumi().getGumiId().equals(rendelesiEgyseg.getGumi().getGumiId())){
                rendelesiEgysegEntity.setMennyiseg(rendelesiEgysegEntity.getMennyiseg() + rendelesiEgyseg.getMennyiseg());
                rendelesiEgysegEntity.setReszosszeg(rendelesiEgysegEntity.getReszosszeg() + rendelesiEgyseg.getReszosszeg());
                meglevo = true;
            }
        }

        if(!meglevo){
            rendeles.getRendelesiEgysegek().add(rendelesiEgyseg);
        }

        try{
            rendelesService.ment(rendeles);
            kosar.getStyle().set("color", "red");
            kosar.setIcon(new Icon(VaadinIcon.CART));
        }catch(Exception ex){
            Notification hibaAblak = new Hibajelzes(ex.getMessage());
            hibaAblak.open();
        }

    }
}
