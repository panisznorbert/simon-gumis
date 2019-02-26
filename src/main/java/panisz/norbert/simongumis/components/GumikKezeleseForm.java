package panisz.norbert.simongumis.components;

import com.helger.commons.annotation.Singleton;
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
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import panisz.norbert.simongumis.LoggerExample;
import panisz.norbert.simongumis.entities.GumiMeretekEntity;
import panisz.norbert.simongumis.entities.GumikEntity;
import panisz.norbert.simongumis.repositories.GumiMeretekRepository;
import panisz.norbert.simongumis.repositories.GumikRepository;

import javax.annotation.PostConstruct;
import javax.persistence.PostLoad;
import java.util.ArrayList;
import java.util.logging.Logger;

@UIScope
@Component
public class GumikKezeleseForm extends VerticalLayout {
    @Autowired
    private GumikRepository gumikRepository;
    @Autowired
    private GumiMeretekRepository gumiMeretekRepository;

    private MenuForm fomenu = new MenuForm();
    private VerticalLayout layout = new VerticalLayout();

    private HorizontalLayout gombok = new HorizontalLayout();
    private HorizontalLayout adatokBevitel = new HorizontalLayout();
    private VerticalLayout adatokMegjelenites = new VerticalLayout();

    private Button hozzaad  = new Button("Hozzáad");
    private Button torol  = new Button("Töröl");

    private Button eltavolit  = new Button("Eltávolít");
    private Button modosit  = new Button("Módosít");

    private final static Logger LOGGER = Logger.getLogger(LoggerExample.class.getName());

    private Grid<GumikEntity> grid = new Grid<>();

    private TextField gyarto = new TextField("Gyártó");
    private TextField meret1 = new TextField("Méret-szélesség");
    private TextField meret2 = new TextField("Méret-profil arány");
    private TextField meret3 = new TextField("Méret-felni átmérő");
    private TextField ar = new TextField("Ár");
    private ComboBox<String> evszak = new ComboBox<>("Évszak", "Téli", "Nyári");
    private ComboBox<String> allapot = new ComboBox<>("Állapot", "Új","Használt");
    private TextField darab  = new TextField("Raktárkészlet");


    private Dialog gumiSzerkeszto;


    public GumikKezeleseForm(){
        init();
        hozzaad.addClickListener(e -> ment());
        torol.addClickListener(e -> mezokInit());
        eltavolit.addClickListener(e -> torles());
        modosit.addClickListener(e -> {
            if(!grid.getSelectedItems().isEmpty()){
                szerkesztes(new ArrayList<>(grid.getSelectedItems()).get(0));
            }else{
                Notification hibaAblak = new HibaJelzes("Nincs kiválasztva módosítandó sor!");
                hibaAblak.open();
        }});
        grid.addItemDoubleClickListener(e -> szerkesztes(e.getItem()));
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



        gombok.add(hozzaad, torol);
        adatokBevitel.add(gyarto, meret1, meret2, meret3, evszak, allapot, ar, darab);
        adatokBevitel.setHeight("100px");
        adatokMegjelenites.add(grid);
        layout.add(gombok, adatokBevitel, adatokMegjelenites, new HorizontalLayout(eltavolit, modosit));
        add(fomenu, layout);
    }

    @PostConstruct
    private void tesztadatok(){
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
    }

    private void ment(){
        String hiba=validacio();
        GumikEntity gumi = new GumikEntity();
        GumiMeretekEntity meret = new GumiMeretekEntity();
        if(hiba == null) {
            meret.setSzelesseg(Integer.valueOf(meret1.getValue()));
            meret.setProfil(Integer.valueOf(meret2.getValue()));
            meret.setFelni(Integer.valueOf(meret3.getValue()));

            //vizsgálni hogy van-e mály ilyen méret lementve és ha igen ne mentsunk még egyet le
            GumiMeretekEntity mentettGumimeret = gumiMeretekRepository.findBySzelessegAndProfilAndFelni(meret.getSzelesseg(), meret.getProfil(), meret.getFelni());
            if (mentettGumimeret != null) {
                LOGGER.info(mentettGumimeret.toString());
                meret = mentettGumimeret;
            }

            gumi.setGyarto(gyarto.getValue());
            gumi.setMeret(meret);
            gumi.setAr(Integer.valueOf(ar.getValue()));
            gumi.setEvszak(evszak.getValue());
            gumi.setAllapot(allapot.getValue());
            gumi.setMennyisegRaktarban(Integer.valueOf(darab.getValue()));

            //vizsgálni, hogy van-e már ilyen gumi lementve, és ha igen akkor ne mentsunk még egyet le

            GumikEntity mentettGumi = gumikRepository.findByGyartoAndMeret_SzelessegAndMeret_ProfilAndMeret_FelniAndEvszakAndAllapot(gumi.getGyarto(), gumi.getMeret().getSzelesseg(), gumi.getMeret().getProfil(), gumi.getMeret().getFelni(), gumi.getEvszak(), gumi.getAllapot());
            if(mentettGumi != null){
                hiba = "Már van ilyen gumi";
            }else{
                gumikRepository.save(gumi);

                grid.setItems(gumikRepository.findAll());
                grid.getDataProvider().refreshAll();
                mezokInit();
            }

        }
        if(hiba != null){
            Notification hibaAblak = new HibaJelzes(hiba);
            hibaAblak.open();
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
            gumikRepository.deleteAll(grid.getSelectedItems());
            gridRefresh();
        }else{
            Notification hibaAblak = new HibaJelzes("Nincs kiválasztva módosítandó sor!");
            hibaAblak.open();
        }
    }

    private void szerkesztes(GumikEntity gumikEntity){
            GumiSzerkesztoForm adatok = new GumiSzerkesztoForm(gumikEntity);
            Button megse  = new Button("Mégse");
            Button ment  = new Button("Módosít");
            HorizontalLayout gombok = new HorizontalLayout(ment, megse);
            gumiSzerkeszto = new Dialog(adatok, gombok);
            gumiSzerkeszto.setCloseOnOutsideClick(false);
            megse.addClickListener(e -> gumiSzerkeszto.close());
            ment.addClickListener(e -> szerkesztesMentese(gumikEntity, adatok));
            gumiSzerkeszto.open();
    }


    private void szerkesztesMentese(GumikEntity gumikEntity, GumiSzerkesztoForm gumiSzerkesztoForm){
        String leiras = gumiSzerkesztoForm.validacio();
        if(leiras == null){
            gumikRepository.save(gumiSzerkesztoForm.beallit(gumikEntity, gumiMeretekRepository));
            gridRefresh();
            gumiSzerkeszto.close();
        }else{
            Notification hibaAblak = new HibaJelzes(leiras);
            hibaAblak.open();
        }
    }

    public void gridRefresh(){
        grid.setItems(gumikRepository.findAll());
        grid.getDataProvider().refreshAll();
    }

}
