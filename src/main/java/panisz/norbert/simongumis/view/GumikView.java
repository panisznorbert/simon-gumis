package panisz.norbert.simongumis.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import org.springframework.beans.factory.annotation.Autowired;
import panisz.norbert.simongumis.entities.GumikEntity;
import panisz.norbert.simongumis.repositories.GumikRepository;

import java.util.ArrayList;


public class GumikView extends HorizontalLayout {

    private GumikRepository alapGumikRepository = null;

    private VerticalLayout layout = new VerticalLayout();

    private HorizontalLayout gombok = new HorizontalLayout();
    private HorizontalLayout adatokBevitel = new HorizontalLayout();
    private VerticalLayout adatokMegjelenites = new VerticalLayout();

    private Button hozzaad  = new Button("Hozzáad");
    private Button torol  = new Button("Töröl");

    private Button eltavolit  = new Button("Eltávolít");
    private Button modosit  = new Button("Módosít");


    private final Grid<GumikEntity> grid = new Grid<>();

    private TextField gyarto = new TextField("Gyártó");
    private TextField meret = new TextField("Méret");
    private TextField ar = new TextField("Ár");
    private ComboBox evszak = new ComboBox("Évszak", "Téli", "Nyári");
    private ComboBox allapot = new ComboBox("Állapot", "Új","Használt");
    private TextField darab  = new TextField("Raktárkészlet");


    private Notification gumiSzerkeszto;

    @Autowired
    public GumikView(GumikRepository gumikRepository){
        this.alapGumikRepository = gumikRepository;
        init();
        hozzaad.addClickListener(e -> ment(alapGumikRepository));
        torol.addClickListener(e -> mezokInit());
        eltavolit.addClickListener(e -> torles());
        modosit.addClickListener(e -> szerkesztes());
    }


    private void init(){
        ar.setPattern("[0-9]*");
        ar.setPreventInvalidInput(true);
        ar.setSuffixComponent(new Span("Ft"));

        gumikTablaFeltolt();

        gombok.add(hozzaad, torol);
        adatokBevitel.add(gyarto, meret, evszak, allapot, ar, darab);
        adatokBevitel.setHeight("100px");
        adatokMegjelenites.add(grid);

        layout.add(gombok, adatokBevitel, adatokMegjelenites, new HorizontalLayout(eltavolit, modosit));


        add(layout);


    }

    private void ment(GumikRepository gumikRepository){
        gumikRepository.save(createGumi(gyarto.getValue().toString(), meret.getValue().toString(), ar.getValue(), evszak.getValue().toString(), allapot.getValue().toString(), darab.getValue()));
        grid.setItems(gumikRepository.findAll());
        grid.getDataProvider().refreshAll();
        //layout.add(grid);
        mezokInit();

    }


    private GumikEntity createGumi (String gyarto, String meret, String ar, String evszak, String allapot, String darab){
        GumikEntity gumi= new GumikEntity();
        gumi.setGyarto(gyarto);
        gumi.setMeret(meret);
        gumi.setAr(Float.valueOf(ar));
        gumi.setEvszak(evszak);
        gumi.setAllapot(allapot);
        gumi.setMennyisegRaktarban(Integer.valueOf(darab));

        return gumi;
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
        meret.clear();
        ar.clear();
        evszak.clear();
        allapot.clear();
        darab.clear();
    }

    private void torles(){
        alapGumikRepository.deleteAll(grid.getSelectedItems());
        gridRefresh();

    }


    private void szerkesztes(){
        if(!grid.getSelectedItems().isEmpty()){
            ArrayList<GumikEntity> gumik = new ArrayList();
            gumik.addAll(grid.getSelectedItems());
            final GumikEntity szerkesztendo = gumik.get(0);
            VerticalLayout adatok = new GumiSzerkesztoView(szerkesztendo);
            Button megse  = new Button("Mégse");
            Button ment  = new Button("Módosít");
            HorizontalLayout gombok = new HorizontalLayout(ment, megse);
            gumiSzerkeszto = new Notification(adatok, gombok);
            gumiSzerkeszto.setPosition(Notification.Position.MIDDLE);
            megse.addClickListener(e -> gumiSzerkeszto.close());
            ment.addClickListener(e -> szerkesztesMentese(((GumiSzerkesztoView) adatok).beallit(szerkesztendo)));
            gumiSzerkeszto.open();


        }else{
            Button kilep = new Button("Ok");
            Label uzenet = new Label("Nincs kiválasztva módosítandó sor!");
            Notification notification = new Notification(uzenet, kilep);
            kilep.addClickListener(event -> notification.close());
            notification.setPosition(Notification.Position.MIDDLE);

            notification.open();
        }

    }

    private void szerkesztesMentese(GumikEntity gumikEntity){
        alapGumikRepository.save(gumikEntity);
        gridRefresh();
        gumiSzerkeszto.close();
    }

    public void gridRefresh(){
        grid.setItems(alapGumikRepository.findAll());
        grid.getDataProvider().refreshAll();
    }


}
