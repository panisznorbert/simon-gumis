package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridContextMenu;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;
import panisz.norbert.simongumis.LoggerExample;
import panisz.norbert.simongumis.entities.GumiMeretekEntity;
import panisz.norbert.simongumis.entities.GumikEntity;
import panisz.norbert.simongumis.exceptions.LetezoGumiException;
import panisz.norbert.simongumis.services.GumikService;
import java.util.logging.Logger;

@UIScope
@Component
public class GumikKezeleseForm extends VerticalLayout {
    private GumikService gumikService;

    private FoMenu fomenu = new FoMenu();
    private VerticalLayout layout = new VerticalLayout();

    private HorizontalLayout gombok = new HorizontalLayout();
    private HorizontalLayout adatokBevitel = new HorizontalLayout();
    private VerticalLayout adatokMegjelenites = new VerticalLayout();

    private Button hozzaad  = new Button("Hozzáad");
    private Button torol  = new Button("Töröl");

    private final static Logger LOGGER = Logger.getLogger(LoggerExample.class.getName());

    private Grid<GumikEntity> grid = new Grid<>();

    private TextField gyarto = new TextField("Gyártó");
    private TextField meret1 = new TextField("Méret-szélesség");
    private TextField meret2 = new TextField("Méret-profil arány");
    private TextField meret3 = new TextField("Méret-felni átmérő");
    private TextField ar = new TextField("Ár");
    private ComboBox<String> evszak = new ComboBox<>("Évszak", "Téli", "Nyári", "Négyévszakos");
    private ComboBox<String> allapot = new ComboBox<>("Állapot", "Új","Használt");
    private TextField darab  = new TextField("Raktárkészlet");

    private Dialog gumiSzerkeszto;


    public GumikKezeleseForm(GumikService gumikService){
        this.gumikService = gumikService;
        this.setAlignItems(Alignment.CENTER);
        grid.setHeightByRows(true);
        hozzaad.addClickListener(e -> ment());
        torol.addClickListener(e -> mezokInit());
        grid.addItemDoubleClickListener(e -> szerkesztes(e.getItem()));
        init();
        fomenu.getGumikKezelese().getStyle().set("color", "blue");
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

        gumikTablaInit();

        gombok.add(hozzaad, torol);
        adatokBevitel.add(gyarto, meret1, meret2, meret3, evszak, allapot, ar, darab);
        adatokBevitel.setHeight("100px");
        adatokMegjelenites.add(grid);
        layout.add(gombok, adatokBevitel, adatokMegjelenites);
        add(fomenu, layout);

        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        gridhezMenu();

    }

    private void gridhezMenu(){
        GridContextMenu<GumikEntity> contextMenu = new GridContextMenu<>(grid);
        contextMenu.addItem("Szerkeszt", event -> szerkesztes(event.getItem().get()));
        contextMenu.addItem("Töröl", event -> torles(event.getItem().get()));

    }

    private void torles(GumikEntity gumikEntity){
        if(grid.getSelectedItems().isEmpty()){
            Notification hibaAblak = new Hibajelzes("Nincs kiválasztva sor");
            hibaAblak.open();
            return;
        }
        gumikService.torol(gumikEntity);
        gridRefresh();
    }

    private void szerkesztes(GumikEntity gumikEntity){
        GumiSzerkeszto adatok = new GumiSzerkeszto(gumikEntity);
        Button megse  = new Button("Mégse");
        Button ment  = new Button("Módosít");
        HorizontalLayout gombok = new HorizontalLayout(ment, megse);
        gumiSzerkeszto = new Dialog(adatok, gombok);
        gumiSzerkeszto.setCloseOnOutsideClick(false);
        megse.addClickListener(e -> gumiSzerkeszto.close());
        ment.addClickListener(e -> szerkesztesMentese(gumikEntity, adatok));
        gumiSzerkeszto.open();

    }

    private void ment(){
        String hiba=validacio();
        GumikEntity gumi = new GumikEntity();

        if(hiba == null) {
            GumiMeretekEntity meret = new GumiMeretekEntity();
            meret.setSzelesseg(Integer.valueOf(meret1.getValue()));
            meret.setProfil(Integer.valueOf(meret2.getValue()));
            meret.setFelni(Integer.valueOf(meret3.getValue()));

            gumi.setGyarto(gyarto.getValue());
            gumi.setMeret(meret);
            gumi.setAr(Integer.valueOf(ar.getValue()));
            gumi.setEvszak(evszak.getValue());
            gumi.setAllapot(allapot.getValue());
            gumi.setMennyisegRaktarban(Integer.valueOf(darab.getValue()));
        }

        try{
            gumikService.ment(gumi);
            gumikTablaFrissit();
            mezokInit();
        }catch(LetezoGumiException ex){
            Notification hibaAblak = new Hibajelzes(ex.getMessage());
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
        GumiGridBeallitas.gumiGridBeallitas(grid);

        if(gumikService.osszes().size()>0){
            gumikTablaFrissit();
        }
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

    private void szerkesztesMentese(GumikEntity gumikEntity, GumiSzerkeszto gumiSzerkeszto){
        String leiras = gumiSzerkeszto.validacio();
        GumikEntity gumi = new GumikEntity();
        if(leiras == null){
            gumi = gumiSzerkeszto.beallit(gumikEntity);
        }

        try{
            gumikService.ment(gumi);
            gridRefresh();
            this.gumiSzerkeszto.close();
        }catch(LetezoGumiException ex){
            Notification hibaAblak = new Hibajelzes(ex.getMessage());
            hibaAblak.open();
        }

    }

    public void gridRefresh(){
        grid.setItems(gumikService.osszes());
        grid.getDataProvider().refreshAll();
    }

}
