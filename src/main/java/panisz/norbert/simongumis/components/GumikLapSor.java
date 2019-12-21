package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import panisz.norbert.simongumis.entities.*;
import panisz.norbert.simongumis.services.AdminService;
import panisz.norbert.simongumis.services.GumikService;
import panisz.norbert.simongumis.services.RendelesService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;

public class GumikLapSor extends HorizontalLayout {

    private AppLayoutMenuItem kosar;

    private VerticalLayout kepHelye = new VerticalLayout();
    private Button gumiKep;
    private VerticalLayout leirasHelye1 = new VerticalLayout();
    private VerticalLayout leirasHelye2 = new VerticalLayout();
    private Label keszlet;
    private VerticalLayout megrendelesHelye = new VerticalLayout();
    private VerticalLayout modositasHelye = new VerticalLayout();
    private Icon szerkeszt;

    GumikLapSor(GumikEntity gumi, GumikService gumikService, RendelesService rendelesService, AppLayoutMenuItem kosar, AdminService adminService){
        this.kosar = kosar;
        ujSor(gumi, gumikService, rendelesService, adminService);

    }

    private void ujSor(GumikEntity gumi, GumikService gumikService, RendelesService rendelesService, AdminService adminService){
        //Gumiról kép
        gumiKep = kepBetolt(gumi.getKep());
        gumiKep.setId("kep-button");

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
        keszlet = new Label("Készleten: " + gumi.getMennyisegRaktarban().toString() + " db");
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
        this.setId("gumik-sor");

        //if (adminService.sessionreKeres(UI.getCurrent().getSession().getSession().getId()) != null){
        Icon torol = new Icon(VaadinIcon.TRASH);
        torol.setId("torol-ikon");
        torol.addClickListener(e -> torles(gumi, gumikService));
        szerkeszt = new Icon(VaadinIcon.EDIT);
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
        this.setId("gumik-sor-szerkesztes");
        this.kepHelye.removeAll();
        this.leirasHelye1.removeAll();
        this.leirasHelye2.removeAll();
        this.remove(megrendelesHelye);
        this.leirasHelye1.setId("leirasHelye1-szerkesztes");
        this.leirasHelye2.setId("leirasHelye2-szerkesztes");
        this.modositasHelye.setId("sorvege-szerkesztes");

        TextField gyarto = new TextField();
        gyarto.setValue(gumi.getGyarto());
        gyarto.setId("gyarto-szerkesztes");
        TextField meret1 = new TextField();
        meret1.setValue(gumi.getMeret().getSzelesseg().toString());
        meret1.setPattern("[0-9]*");
        TextField meret2 = new TextField();
        meret2.setValue(gumi.getMeret().getProfil().toString());
        meret2.setPattern("[0-9]*");
        TextField meret3 = new TextField();
        meret3.setValue(gumi.getMeret().getFelni().toString());
        meret3.setPattern("[0-9]*");
        Label meretKiegeszites1 = new Label("/");
        Label meretKiegeszites2 = new Label("R");
        meretKiegeszites1.setId("meretKiegeszites");
        meretKiegeszites2.setId("meretKiegeszites");
        HorizontalLayout meret = new HorizontalLayout(meret1, meretKiegeszites1, meret2, meretKiegeszites2, meret3);

        meret.setId("meret-szerkesztes");
        ComboBox<String> evszak = new ComboBox<>("", "Téli", "Nyári", "Négyévszakos");
        evszak.setValue(gumi.getEvszak());
        ComboBox<String> allapot = new ComboBox<>("", "Új","Használt");
        allapot.setValue(gumi.getAllapot());
        leirasHelye1.add(gyarto, meret, evszak, allapot);

        TextField ar = new TextField();
        ar.setValue(gumi.getAr().toString());
        ar.setId("ar-szerkesztes");
        ar.setSuffixComponent(new Span("Ft/db"));
        TextField darab  = new TextField();
        darab.setValue(gumi.getMennyisegRaktarban().toString());
        darab.setId("darab-szerkesztes");
        darab.setSuffixComponent(new Span("db"));
        keszlet.setText("Keszleten:");
        HorizontalLayout keszletSor = new HorizontalLayout(keszlet, darab);
        keszletSor.setId("keszletSor-szerkesztes");
        leirasHelye2.add(keszletSor, ar);

        modositasHelye.remove(szerkeszt);
        szerkeszt = new Icon(VaadinIcon.CLIPBOARD_CHECK);
        szerkeszt.setId("mentes-ikon");
        szerkeszt.addClickListener(e -> szerkesztesMentese());
        modositasHelye.add(szerkeszt);

        MemoryBuffer memoryBuffer = new MemoryBuffer();
        Upload imgUpload = new Upload(memoryBuffer);
        imgUpload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
        imgUpload.setDropLabel(new Label("Húzza ide a fájlt"));
        imgUpload.setUploadButton(new Icon(VaadinIcon.FILE_ADD));
        imgUpload.setWidth("200px");

        kepHelye.setId("kepHelye-szerkesztes");
        VerticalLayout kep = new VerticalLayout(gumiKep);
        kep.setId("kepHelye");
        this.kepHelye.add(kep, imgUpload);
        imgUpload.addSucceededListener(e -> {
            System.out.println("Succeeded Upload of " + e.getFileName());
            try {
                gumiKep = kepBetolt(memoryBuffer.getInputStream().readAllBytes());
                kep.removeAll();
                kep.add(gumiKep);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });

    }

    private void szerkesztesMentese(){}

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

    private Button kepBetolt(byte[] gumikEntity){
        Button kep = new Button();
        if(gumikEntity != null){
            StreamResource streamResource = new StreamResource("isr", new InputStreamFactory() {
                @Override
                public InputStream createInputStream() {
                    return new ByteArrayInputStream(gumikEntity);
                }
            });
            kep.setIcon(new Image(streamResource, ""));
            kep.addClickListener(e -> sorkivalasztas(gumikEntity));
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
