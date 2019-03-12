package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
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
import panisz.norbert.simongumis.services.implement.GumiMeretekServiceImpl;
import panisz.norbert.simongumis.services.implement.GumikServiceImpl;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.logging.Logger;

@UIScope
@Component
public class GumikKezeleseForm extends VerticalLayout {
    @Autowired
    private GumikServiceImpl gumikService;
    @Autowired
    private GumiMeretekServiceImpl gumiMeretekService;

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


    @PostConstruct
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

        gumikTablaInit();

        gombok.add(hozzaad, torol);
        adatokBevitel.add(gyarto, meret1, meret2, meret3, evszak, allapot, ar, darab);
        adatokBevitel.setHeight("100px");
        adatokMegjelenites.add(grid);
        layout.add(gombok, adatokBevitel, adatokMegjelenites, new HorizontalLayout(eltavolit, modosit));
        add(fomenu, layout);
    }


    private void ment(){
        String hiba=validacio();
        GumikEntity gumi = new GumikEntity();

        if(hiba == null) {
            //vizsgálni hogy van-e már ilyen méret lementve és ha igen ne mentsunk még egyet le
            GumiMeretekEntity meret = gumiMeretekService.mindenMeretreKeres(Integer.valueOf(meret1.getValue()), Integer.valueOf(meret2.getValue()), Integer.valueOf(meret3.getValue()));
            if (meret == null) {
                meret = new GumiMeretekEntity();
                meret.setSzelesseg(Integer.valueOf(meret1.getValue()));
                meret.setProfil(Integer.valueOf(meret2.getValue()));
                meret.setFelni(Integer.valueOf(meret3.getValue()));
            }

            gumi.setGyarto(gyarto.getValue());
            gumi.setMeret(meret);
            gumi.setAr(Integer.valueOf(ar.getValue()));
            gumi.setEvszak(evszak.getValue());
            gumi.setAllapot(allapot.getValue());
            gumi.setMennyisegRaktarban(Integer.valueOf(darab.getValue()));

            //vizsgálni, hogy van-e már ilyen gumi lementve, és ha igen akkor ne mentsunk még egyet le

            GumikEntity mentettGumi = gumikService.vanMarIlyen(gumi.getGyarto(), gumi.getMeret().getSzelesseg(), gumi.getMeret().getProfil(), gumi.getMeret().getFelni(), gumi.getEvszak(), gumi.getAllapot());
            if(mentettGumi != null){
                hiba = "Már van ilyen gumi";
            }else{
                LOGGER.info("Gumi id: " + gumi.getId() + ", Gumi méret id: " + gumi.getMeret().getId());
                gumikService.ment(gumi);

                gumikTablaFrissit();
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

    private void gumikTablaInit(){
        grid.addColumn(GumikEntity::getGyarto).setHeader("Gyártó");
        grid.addColumn(GumikEntity::getMeret).setHeader("Méret");
        grid.addColumn(GumikEntity::getEvszak).setHeader("Évszak");
        grid.addColumn(GumikEntity::getAllapot).setHeader("Állapot");
        grid.addColumn(GumikEntity::getAr).setHeader("Ár");
        grid.addColumn(GumikEntity::getMennyisegRaktarban).setHeader("Raktáron (db)");

        //if(gumikRepository.findAll().size()>0){
            gumikTablaFrissit();
       // }
    }

    private void gumikTablaFrissit(){
        grid.setItems(gumikService.osszes());
        grid.getDataProvider().refreshAll();
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
            gumikService.torolMind(grid.getSelectedItems());
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
            GumikEntity gumi = gumiSzerkesztoForm.beallit(gumikEntity);
            if(!gumikEntity.getMeret().equals(gumi.getMeret())){
                //ha már létező gumi van akkor azt megvizsgálni és azzal menteni

                GumiMeretekEntity ujGumiMeret = gumiMeretekService.mindenMeretreKeres(gumi.getMeret().getSzelesseg(), gumi.getMeret().getProfil(), gumi.getMeret().getFelni());
                if(ujGumiMeret == null) {
                    ujGumiMeret = new GumiMeretekEntity();
                    ujGumiMeret.setSzelesseg(gumi.getMeret().getSzelesseg());
                    ujGumiMeret.setProfil(gumi.getMeret().getProfil());
                    ujGumiMeret.setFelni(gumi.getMeret().getFelni());
                }
                gumi.setMeret(ujGumiMeret);
            }
            gumikService.ment(gumi);
            gridRefresh();
            gumiSzerkeszto.close();
        }else{
            Notification hibaAblak = new HibaJelzes(leiras);
            hibaAblak.open();
        }
    }

    public void gridRefresh(){
        grid.setItems(gumikService.osszes());
        grid.getDataProvider().refreshAll();
    }

}
