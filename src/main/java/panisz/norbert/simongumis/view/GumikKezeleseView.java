package panisz.norbert.simongumis.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import panisz.norbert.simongumis.entities.GumiMeretekEntity;
import panisz.norbert.simongumis.entities.GumikEntity;
import panisz.norbert.simongumis.repositories.GumiMeretekRepository;
import panisz.norbert.simongumis.repositories.GumikRepository;
import java.util.ArrayList;


public class GumikKezeleseView extends HorizontalLayout {

    private GumikRepository alapGumikRepository = null;
    private GumiMeretekRepository alapGumiMeretekRepository = null;

    private VerticalLayout layout = new VerticalLayout();

    private HorizontalLayout gombok = new HorizontalLayout();
    private HorizontalLayout adatokBevitel = new HorizontalLayout();
    private VerticalLayout adatokMegjelenites = new VerticalLayout();

    private Button hozzaad  = new Button("Hozzáad");
    private Button torol  = new Button("Töröl");

    private Button eltavolit  = new Button("Eltávolít");
    private Button modosit  = new Button("Módosít");


    private Grid<GumikEntity> grid = new Grid<>();

    private TextField gyarto = new TextField("Gyártó");
    private TextField meret1 = new TextField("Méret-szélesség");
    private TextField meret2 = new TextField("Méret-profil arány");
    private TextField meret3 = new TextField("Méret-felni átmérő");
    private TextField ar = new TextField("Ár");
    private ComboBox evszak = new ComboBox("Évszak", "Téli", "Nyári");
    private ComboBox allapot = new ComboBox("Állapot", "Új","Használt");
    private TextField darab  = new TextField("Raktárkészlet");


    private Dialog gumiSzerkeszto;

    @Autowired
    public GumikKezeleseView(GumikRepository gumikRepository, GumiMeretekRepository gumiMeretekRepository){
        this.alapGumikRepository = gumikRepository;
        this.alapGumiMeretekRepository = gumiMeretekRepository;
        init();
        hozzaad.addClickListener(e -> ment());
        torol.addClickListener(e -> mezokInit());
        eltavolit.addClickListener(e -> torles());
        modosit.addClickListener(e -> szerkesztes());
    }


    private void init(){
        ar.setPattern("\\d*(\\.\\d*)?");
        ar.setPreventInvalidInput(true);
        ar.setSuffixComponent(new Span("Ft"));
        darab.setPattern("[0-9]*");
        darab.setPreventInvalidInput(true);
        darab.setSuffixComponent(new Span("Db"));
        evszak.setValue("Nyári");
        allapot.setValue("Új");

        gyarto.setRequired(true);
        ar.setRequired(true);
        meret1.setRequired(true);
        meret2.setRequired(true);
        meret3.setRequired(true);
        evszak.setRequired(true);
        allapot.setRequired(true);
        darab.setRequired(true);

        gumikTablaFeltolt();

        //alapadatok hozzáadása
        gyarto.setValue("Gyártó1");
        meret1.setValue("155");
        meret2.setValue("40");
        meret3.setValue("20");
        ar.setValue("4500");
        evszak.setValue("Téli");
        allapot.setValue("Új");
        darab.setValue("4");
        ment();
        gyarto.setValue("Gyártó2");
        meret1.setValue("155");
        meret2.setValue("50");
        meret3.setValue("19");
        ar.setValue("4500");
        evszak.setValue("Téli");
        allapot.setValue("Új");
        darab.setValue("4");
        ment();
        gyarto.setValue("Gyártó3");
        meret1.setValue("255");
        meret2.setValue("40");
        meret3.setValue("21");
        ar.setValue("4500");
        evszak.setValue("Téli");
        allapot.setValue("Új");
        darab.setValue("4");
        ment();
        gyarto.setValue("Gyártó2");
        meret1.setValue("215");
        meret2.setValue("30");
        meret3.setValue("21");
        ar.setValue("4000");
        evszak.setValue("Nyári");
        allapot.setValue("Új");
        darab.setValue("6");
        ment();


        gombok.add(hozzaad, torol);
        adatokBevitel.add(gyarto, meret1, meret2, meret3, evszak, allapot, ar, darab);
        adatokBevitel.setHeight("100px");
        adatokMegjelenites.add(grid);
        layout.add(gombok, adatokBevitel, adatokMegjelenites, new HorizontalLayout(eltavolit, modosit));
        add(layout);
    }

    private void ment(){
        String hiba=validacio();
        if(hiba==null){
            GumikEntity gumi = new GumikEntity();
            GumiMeretekEntity meret = new GumiMeretekEntity();
            meret.setSzelesseg(Integer.valueOf(meret1.getValue()));
            meret.setProfil(Integer.valueOf(meret2.getValue()));
            meret.setFelni(Integer.valueOf(meret3.getValue()));

            gumi.setGyarto(gyarto.getValue());
            gumi.setMeret(meret);
            gumi.setAr(Integer.valueOf(ar.getValue()));
            gumi.setEvszak(evszak.getValue().toString());
            gumi.setAllapot(allapot.getValue().toString());
            gumi.setMennyisegRaktarban(Integer.valueOf(darab.getValue()));
            alapGumikRepository.save(gumi);

            grid.setItems(alapGumikRepository.findAll());
            grid.getDataProvider().refreshAll();
            mezokInit();
        }else{
            openNotification(hiba);
        }
    }


    private String validacio() {
        return getString(gyarto, evszak, allapot, ar, darab, meret1, meret2, meret3);
    }

    static String getString(TextField gyarto, ComboBox evszak, ComboBox allapot, TextField ar, TextField darab, TextField meret1, TextField meret2, TextField meret3) {
        if(gyarto.isEmpty()){
            return("Minden mező kitöltése kötelező!");
        }
        if(evszak.isEmpty()){
            return("Minden mező kitöltése kötelező!");
        }
        if(allapot.isEmpty()){
            return("Minden mező kitöltése kötelező!");
        }
        if(ar.isEmpty()){
            return("Minden mező kitöltése kötelező!");
        }
        if(darab.isEmpty()){
            return("Minden mező kitöltése kötelező!");
        }
        if(meret1.isEmpty() || Integer.valueOf(meret1.getValue())<135 || Integer.valueOf(meret1.getValue())>315 || (Integer.valueOf(meret1.getValue())%5)!=0 || (Integer.valueOf(meret1.getValue())%10)==0){
            return("A méret-szélesség hibásan lett megadva!");
        }
        if(meret2.isEmpty() || Integer.valueOf(meret2.getValue())<25 || Integer.valueOf(meret2.getValue())>80 || (Integer.valueOf(meret2.getValue())%5)!=0){
            return("A méret-profil hibásan lett megadva!");
        }
        if(meret3.isEmpty() || Integer.valueOf(meret3.getValue())<10 || Integer.valueOf(meret3.getValue())>21 ){
            return("A méret-felni átmérő hibásan lett megadva!");
        }

        return null;
    }

    private void gumikTablaFeltolt(){
        grid.addColumn(GumikEntity::getGyarto).setHeader("Gyártó");
        grid.addColumn(GumikEntity::getMeret).setHeader("Méret");
        grid.addColumn(GumikEntity::getEvszak).setHeader("Évszak");
        grid.addColumn(GumikEntity::getAllapot).setHeader("Állapot");
        grid.addColumn(GumikEntity::getAr).setHeader("Ár");
        grid.addColumn(GumikEntity::getMennyisegRaktarban).setHeader("Raktáron (db)");

    }

    private void mezokInit(){
        gyarto.clear();
        meret1.clear();
        meret2.clear();
        meret3.clear();
        ar.clear();
        evszak.setValue("Nyári");
        allapot.setValue("Új");
        darab.clear();
    }

    private void torles(){
        if(!grid.getSelectedItems().isEmpty()) {
            alapGumikRepository.deleteAll(grid.getSelectedItems());
            gridRefresh();
        }else{
            openNotification("Nincs kiválasztva módosítandó sor!");
        }
    }

    private void szerkesztes(){
        if(!grid.getSelectedItems().isEmpty()){
            ArrayList<GumikEntity> gumik = new ArrayList();
            gumik.addAll(grid.getSelectedItems());
            GumikEntity szerkesztendo = gumik.get(0);
            GumiSzerkesztoView adatok = new GumiSzerkesztoView(szerkesztendo);
            Button megse  = new Button("Mégse");
            Button ment  = new Button("Módosít");
            HorizontalLayout gombok = new HorizontalLayout(ment, megse);
            gumiSzerkeszto = new Dialog(adatok, gombok);
            gumiSzerkeszto.setCloseOnOutsideClick(false);
            megse.addClickListener(e -> gumiSzerkeszto.close());
            ment.addClickListener(e -> szerkesztesMentese(szerkesztendo, adatok));
            gumiSzerkeszto.open();
        }else{
            openNotification("Nincs kiválasztva módosítandó sor!");
        }
    }

    private void openNotification(String uzenet){
        Button kilep = new Button("Ok");
        Label leiras = new Label(uzenet);
        Notification notification = new Notification(leiras, kilep);
        kilep.addClickListener(event -> notification.close());
        notification.setPosition(Notification.Position.MIDDLE);
        notification.open();
    }

    private void szerkesztesMentese(GumikEntity gumikEntity, GumiSzerkesztoView gumiSzerkesztoView){
        String leiras = gumiSzerkesztoView.validacio();
        if(leiras == null){
            alapGumikRepository.save(gumiSzerkesztoView.beallit(gumikEntity, alapGumiMeretekRepository));
            gridRefresh();
            gumiSzerkeszto.close();
        }else{
            openNotification(leiras);
        }
    }

    public void gridRefresh(){
        grid.setItems(alapGumikRepository.findAll());
        grid.getDataProvider().refreshAll();
    }

}
