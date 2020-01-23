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
import panisz.norbert.simongumis.exceptions.HibasKitoltesException;
import panisz.norbert.simongumis.exceptions.LetezoGumiException;
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

    private GumikService gumikService;
    private RendelesService rendelesService;
    private AdminService adminService;

    private VerticalLayout kepHelye = new VerticalLayout();
    private Button gumiKep;
    private VerticalLayout leirasHelye1 = new VerticalLayout();
    private VerticalLayout leirasHelye2 = new VerticalLayout();
    private Label keszlet = new Label();
    private VerticalLayout megrendelesHelye = new VerticalLayout();
    private VerticalLayout modositasHelye = new VerticalLayout();
    private Icon szerkeszt;
    private Icon torol;

    private MemoryBuffer memoryBuffer;
    private TextField gyarto = new TextField();
    private TextField meret1 = new TextField();
    private TextField meret2 = new TextField();
    private TextField meret3 = new TextField();
    private Label meretKiegeszites1 = new Label("/");
    private Label meretKiegeszites2 = new Label("R");
    private HorizontalLayout meret = new HorizontalLayout(meret1, meretKiegeszites1, meret2, meretKiegeszites2, meret3);
    private ComboBox<String> evszak = new ComboBox<>("", "Téli", "Nyári", "Négyévszakos");
    private ComboBox<String> allapot = new ComboBox<>("", "Új","Használt");
    private TextField ar = new TextField();
    private TextField darab  = new TextField();


    GumikLapSor(GumikEntity gumi, GumikService gumikService, RendelesService rendelesService, AppLayoutMenuItem kosar, AdminService adminService){
        this.kosar = kosar;
        this.gumikService = gumikService;
        this.rendelesService = rendelesService;
        this.adminService = adminService;
        ujSor(gumi);

    }

    GumikLapSor(GumikService gumikService, RendelesService rendelesService, AppLayoutMenuItem kosar, AdminService adminService){
        this.kosar = kosar;
        this.gumikService = gumikService;
        this.rendelesService = rendelesService;
        this.adminService = adminService;
        ujElemFelvetele();

    }

    private void setKepHelyeSzerkeszt(String funkcio){
        memoryBuffer = new MemoryBuffer();
        Upload imgUpload = new Upload(memoryBuffer);
        imgUpload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
        imgUpload.setDropLabel(new Label("Húzza ide a fájlt"));
        imgUpload.setUploadButton(new Icon(VaadinIcon.FILE_ADD));
        imgUpload.setWidth("200px");

        if("szerkeszt".equals(funkcio)){
            imgUpload.setId("kepfeltolto");
        }
        if("uj".equals(funkcio)){
            imgUpload.setId("kepfeltolto-uj");
        }

        kepHelye.setId("kepHelye-szerkesztes");
        VerticalLayout kep = new VerticalLayout(gumiKep);
        kep.setId("kepHelye");
        kepHelye.add(kep, imgUpload);
        imgUpload.addSucceededListener(e -> {
            try {
                gumiKep = kepBetolt(memoryBuffer.getInputStream().readAllBytes());
                kep.removeAll();
                kep.add(gumiKep);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void ujElemFelvetele(){
        this.setId("uj-elem-felvetele-sor");
        this.leirasHelye1.setId("leirasHelye1-uj-elem");
        this.leirasHelye2.setId("leirasHelye2-uj-elem");

        gumiKep = kepBetolt(null);
        gumiKep.setId("kep-button-uj");
        kepHelye.setSizeUndefined();
        setKepHelyeSzerkeszt("uj");

        szerkeszthetoMezokInit();
        gyarto.setId("gyarto-uj-elem");
        ar.setId("ar-uj-elem");
        darab.setId("darab-uj-elem");

        gyarto.setPlaceholder("Gyártó");
        evszak.setPlaceholder("Évszak");
        allapot.setPlaceholder("Állapot");

        Icon ment = new Icon(VaadinIcon.CLIPBOARD_CHECK);
        ment.setId("uj-mentes-ikon");
        ment.addClickListener(e -> mentes(null));

        leirasHelye2.add(darab, ar, ment);

        //Sorok felépítése
        this.add(kepHelye, leirasHelye1, leirasHelye2);
    }

    private void ujSor(GumikEntity gumi){
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
        keszlet.setText("Készleten: " + gumi.getMennyisegRaktarban().toString() + " db");
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
        torol = new Icon(VaadinIcon.TRASH);
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
        this.modositasHelye.removeAll();

        torol = new Icon(VaadinIcon.FILE_REMOVE);
        torol.setId("torol-ikon");
        torol.addClickListener(e -> szerkesztesMegse(gumi));

        modositasHelye.add(torol, szerkeszt);
        modositasHelye.setSizeUndefined();

        szerkeszthetoMezokInit();
        gyarto.setId("gyarto-szerkesztes");
        ar.setId("ar-szerkesztes");
        darab.setId("darab-szerkesztes");

        gyarto.setValue(gumi.getGyarto());
        meret1.setValue(gumi.getMeret().getSzelesseg().toString());
        meret2.setValue(gumi.getMeret().getProfil().toString());
        meret3.setValue(gumi.getMeret().getFelni().toString());
        evszak.setValue(gumi.getEvszak());
        allapot.setValue(gumi.getAllapot());
        ar.setValue(gumi.getAr().toString());
        darab.setValue(gumi.getMennyisegRaktarban().toString());

        keszlet.setText("Keszleten:");
        HorizontalLayout keszletSor = new HorizontalLayout(keszlet, darab);
        keszletSor.setId("keszletSor-szerkesztes");
        leirasHelye2.add(keszletSor, ar);

        modositasHelye.remove(szerkeszt);
        szerkeszt = new Icon(VaadinIcon.CLIPBOARD_CHECK);
        szerkeszt.setId("mentes-ikon");
        szerkeszt.addClickListener(e -> mentes(gumi));
        modositasHelye.add(szerkeszt);

        setKepHelyeSzerkeszt("szerkeszt");

    }

    private void szerkeszthetoMezokInit(){
        meret1.setPattern("[0-9]*");
        meret2.setPattern("[0-9]*");
        meret3.setPattern("[0-9]*");
        meretKiegeszites1.setId("meretKiegeszites");
        meretKiegeszites2.setId("meretKiegeszites");
        meret.setId("meret-szerkesztes");
        evszak.setId("combobox-szerkesztes");
        allapot.setId("combobox-szerkesztes");
        ar.setSuffixComponent(new Span("Ft/db"));
        darab.setSuffixComponent(new Span("db"));
        leirasHelye1.add(gyarto, meret, evszak, allapot);
    }

    private void szerkesztesMegse(GumikEntity gumi){
        this.removeAll();
        this.kepHelye.removeAll();
        this.leirasHelye1.removeAll();
        this.leirasHelye2.removeAll();
        this.megrendelesHelye.removeAll();
        this.modositasHelye.removeAll();
        this.remove(megrendelesHelye);
        this.ujSor(gumi);
    }

    private GumikEntity hibasKitoltes(GumikEntity gumi) throws HibasKitoltesException {
        if(gumi == null){
            gumi = new GumikEntity();
            gumi.setMeret(new GumiMeretekEntity());
        }

        if(gyarto.isEmpty()){
            throw new HibasKitoltesException("Gyártó mező kitöltése kötelező!");
        }
        if(evszak.isEmpty()){
            throw new HibasKitoltesException("Évszak mező kitöltése kötelező!");
        }
        if(allapot.isEmpty()){
            throw new HibasKitoltesException("Állapot mező kitöltése kötelező!");
        }
        if(ar.isEmpty()){
            throw new HibasKitoltesException("Ár mező kitöltése kötelező!");
        }
        if(darab.isEmpty()){
            throw new HibasKitoltesException("Darab mező kitöltése kötelező!");
        }
        if(meret1.isEmpty() || Integer.parseInt(meret1.getValue())<135 || Integer.parseInt(meret1.getValue())>315 || (Integer.parseInt(meret1.getValue())%5)!=0 || (Integer.parseInt(meret1.getValue())%10)==0){
            throw new HibasKitoltesException("A méret-szélesség hibásan lett megadva!");
        }
        if(meret2.isEmpty() || Integer.parseInt(meret2.getValue())<25 || Integer.parseInt(meret2.getValue())>80 || (Integer.parseInt(meret2.getValue())%5)!=0){
            throw new HibasKitoltesException("A méret-profil hibásan lett megadva!");
        }
        if(meret3.isEmpty() || Integer.parseInt(meret3.getValue())<10 || Integer.parseInt(meret3.getValue())>21 ){
            throw new HibasKitoltesException("A méret-felni átmérő hibásan lett megadva!");
        }

        if(memoryBuffer != null){
            gumi.setKep(memoryBuffer);
        }

        gumi.setGyarto(gyarto.getValue());
        gumi.setEvszak(evszak.getValue());
        gumi.setAllapot(allapot.getValue());
        gumi.setAr(Integer.valueOf(ar.getValue()));
        gumi.setMennyisegRaktarban(Integer.valueOf(darab.getValue()));
        gumi.getMeret().setSzelesseg(Integer.valueOf(meret1.getValue()));
        gumi.getMeret().setProfil(Integer.valueOf(meret2.getValue()));
        gumi.getMeret().setFelni(Integer.valueOf(meret3.getValue()));

        return gumi;
    }


    private void mentes(GumikEntity gumi){
        try{
            gumikService.ment(hibasKitoltes(gumi));
            if(gumi != null){
                this.removeAll();
                szerkesztesMegse(gumi);
            }else{
                UI.getCurrent().getPage().reload();
            }
        }catch(LetezoGumiException ex){
            Notification hibaAblak = new Hibajelzes("Ilyen gumi már létezik: " + gumi.toString());
            Button hozzaad = new Button(gumi.getMennyisegRaktarban() + " db gumi hozzáadása");
            hozzaad.addClickListener(e -> {
                darabszamEmelese(Integer.valueOf(ex.getMessage()), gumi);
                hibaAblak.close();
            });
            hibaAblak.add(hozzaad);
            hibaAblak.open();
        }catch(HibasKitoltesException ex){
            Hibajelzes hibajelzes = new Hibajelzes(ex.getMessage());
            hibajelzes.open();
        }catch(Exception ex) {
            ex.printStackTrace();
            Notification hibaAblak = new Hibajelzes("Sikertelen mentés");
            hibaAblak.open();
        }
    }

    private void darabszamEmelese(Integer id, GumikEntity gumi){
        try{
            gumi.setId(id);
            gumi.setMennyisegRaktarban(gumi.getMennyisegRaktarban() + gumikService.idraKereses(id).getMennyisegRaktarban());
            gumikService.ment(gumi);
            this.removeAll();
            szerkesztesMegse(gumi);
        }catch(Exception ex) {
            Notification hibaAblak = new Hibajelzes(ex.getMessage());
            hibaAblak.open();
        }
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
