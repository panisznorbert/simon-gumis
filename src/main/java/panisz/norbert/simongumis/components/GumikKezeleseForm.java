package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridContextMenu;
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
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;
import panisz.norbert.simongumis.entities.GumiMeretekEntity;
import panisz.norbert.simongumis.entities.GumikEntity;
import panisz.norbert.simongumis.exceptions.LetezoGumiException;
import panisz.norbert.simongumis.services.GumikService;

import java.util.logging.Logger;

@UIScope
@Component
public class GumikKezeleseForm extends VerticalLayout {

    private final static Logger LOGGER = Logger.getLogger(GumikKezeleseForm.class.getName());

    private GumikService gumikService;

    private Button hozzaad  = new Button("Hozzáad");

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

    private MemoryBuffer memoryBuffer = new MemoryBuffer();
    private Upload imgUpload;

    public GumikKezeleseForm(GumikService gumikService){

        this.getStyle().set("width", "80%");
        this.getStyle().set("padding-left", "20%");
        this.gumikService = gumikService;
        grid.setHeightByRows(true);
        hozzaad.addClickListener(e -> ment());
        grid.addItemDoubleClickListener(e -> szerkesztes(e.getItem()));
        grid.addItemClickListener(e -> sorkivalasztas(e.getItem()));
        this.setAlignItems(Alignment.CENTER);
        init();
    }

    private void uploadInit(){
        imgUpload = new Upload(memoryBuffer);
        imgUpload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
        imgUpload.setDropLabel(new Label("Húzza ide a fájlt"));
        imgUpload.setUploadButton(new Icon(VaadinIcon.FILE_ADD));

    }

    private void init(){
        ar.setPattern("\\d*(\\.\\d*)?");
        ar.setPreventInvalidInput(true);
        ar.setSuffixComponent(new Span("Ft"));
        darab.setPattern("[0-9]*");
        darab.setPreventInvalidInput(true);
        darab.setSuffixComponent(new Span("Darab"));
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
        uploadInit();

        HorizontalLayout adatokBevitel = new HorizontalLayout();
        HorizontalLayout menusor1 = new HorizontalLayout(gyarto, meret1, meret2, meret3);
        HorizontalLayout menusor2 = new HorizontalLayout(evszak, allapot, ar, darab);

        imgUpload.setWidth("200px");
        adatokBevitel.setHeight("200px");
        adatokBevitel.setAlignItems(Alignment.CENTER);
        grid.setWidth("1200px");
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);

        adatokBevitel.add(new VerticalLayout(menusor1, menusor2), new HorizontalLayout(imgUpload), new HorizontalLayout(hozzaad));

        add(adatokBevitel, new HorizontalLayout(grid));

        gridhezMenu();
    }

    private void gridhezMenu(){
        GridContextMenu<GumikEntity> contextMenu = new GridContextMenu<>(grid);
        contextMenu.addItem("Szerkeszt", event -> szerkesztes(event.getItem().get()));
        contextMenu.addItem("Töröl", event -> torles(event.getItem().get()));
    }

    private void sorkivalasztas(GumikEntity gumikEntity){
        gyarto.setValue(gumikEntity.getGyarto());
        meret1.setValue(gumikEntity.getMeret().getSzelesseg().toString());
        meret2.setValue(gumikEntity.getMeret().getProfil().toString());
        meret3.setValue(gumikEntity.getMeret().getFelni().toString());
        evszak.setValue(gumikEntity.getEvszak());
        allapot.setValue(gumikEntity.getAllapot());
        ar.setValue(gumikEntity.getAr().toString());
        darab.setValue(gumikEntity.getMennyisegRaktarban().toString());
    }

    private void torles(GumikEntity gumikEntity){
        if(grid.getSelectedItems().isEmpty()){
            Notification hibaAblak = new Hibajelzes("Nincs kiválasztva sor");
            hibaAblak.open();
            return;
        }
        gumikService.torol(gumikEntity);
        gumikTablaFrissit();
    }

    private void szerkesztes(GumikEntity gumikEntity){
        GumiSzerkeszto adatok = new GumiSzerkeszto(gumikEntity);
        Button megse  = new Button("Mégse");
        Button ment  = new Button("Módosít");
        HorizontalLayout gombok = new HorizontalLayout(ment, megse);
        gumiSzerkeszto = new Dialog(adatok, gombok);
        gumiSzerkeszto.setWidth("750px");
        gumiSzerkeszto.setCloseOnOutsideClick(false);
        megse.addClickListener(e -> gumiSzerkeszto.close());
        ment.addClickListener(e -> szerkesztesMentese(gumikEntity, adatok));
        gumiSzerkeszto.open();
    }

    private void ment(){
        String hiba=validacio();
        GumikEntity gumi = new GumikEntity();

        if(hiba != null){
            Notification hibaAblak = new Hibajelzes(hiba);
            hibaAblak.open();
            return;
        }

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

        if(!memoryBuffer.getFileName().isEmpty()){
            try{
                gumi.setKep(memoryBuffer.getInputStream().readAllBytes());
            }catch (Exception ex){
                Notification hibaAblak = new Hibajelzes("A kép mentése sikertelen");
                hibaAblak.open();
            }
        }

        kimentes(gumi);
    }

    private void kimentes(GumikEntity gumi){
        try{
            gumikService.ment(gumi);
            gumikTablaFrissit();
            mezokInit();
            gumiSzerkeszto.close();
            //UI.getCurrent().getPage().reload();

        }catch(LetezoGumiException ex){
            Notification hibaAblak = new Hibajelzes("Ilyen gumi már létezik: " + gumi.toString());
            Button hozzaad = new Button(gumi.getMennyisegRaktarban() + " db gumi hozzáadása");
            hozzaad.addClickListener(e -> {
                darabszamEmelese(Integer.valueOf(ex.getMessage()), gumi);
                hibaAblak.close();
            });
            hibaAblak.add(hozzaad);
            hibaAblak.open();
        }catch(Exception ex) {
            LOGGER.info("Sikertelene mentés hiba: " + ex.getMessage());
            Notification hibaAblak = new Hibajelzes("Sikertelen mentés");
            hibaAblak.open();
        }
    }

    private void darabszamEmelese(Integer id, GumikEntity gumi){
        try{
            gumi.setId(id);
            gumi.setMennyisegRaktarban(gumi.getMennyisegRaktarban() + gumikService.idraKereses(id).getMennyisegRaktarban());
            gumikService.ment(gumi);
            gumikTablaFrissit();
            mezokInit();
        }catch(Exception ex) {
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
        if(meret1.isEmpty() || Integer.parseInt(meret1.getValue())<135 || Integer.parseInt(meret1.getValue())>315 || (Integer.parseInt(meret1.getValue())%5)!=0 || (Integer.parseInt(meret1.getValue())%10)==0){
            return("A méret-szélesség hibásan lett megadva!");
        }
        if(meret2.isEmpty() || Integer.parseInt(meret2.getValue())<25 || Integer.parseInt(meret2.getValue())>80 || (Integer.parseInt(meret2.getValue())%5)!=0){
            return("A méret-profil hibásan lett megadva!");
        }
        if(meret3.isEmpty() || Integer.parseInt(meret3.getValue())<10 || Integer.parseInt(meret3.getValue())>21 ){
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

        if(leiras == null){
            kimentes(gumiSzerkeszto.beallit(gumikEntity));
        }else{
            Notification hibaAblak = new Hibajelzes(leiras);
            hibaAblak.open();
        }

    }

}
