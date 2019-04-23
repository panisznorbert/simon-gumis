package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;
import panisz.norbert.simongumis.LoggerExample;
import panisz.norbert.simongumis.entities.*;
import panisz.norbert.simongumis.services.RendelesService;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

@UIScope
@Component
public class KosarForm extends VerticalLayout {

    private static RendelesService alapRendelesService;

    private MenuForm fomenu  = new MenuForm();

    private RendelesEntity rendelesEntity;
    private VerticalLayout tartalom = new VerticalLayout();
    private Grid<RendelesiEgysegEntity> rendelesekTabla = new Grid<>();

    private UgyfelMezok vevoAdatai = new UgyfelMezok();
    private Button megrendeles = new Button("Megrendelés");
    private HorizontalLayout gombok = new HorizontalLayout(megrendeles);
    private TextField vegosszeg = new TextField("Végösszeg:");

    private final static Logger LOGGER = Logger.getLogger(LoggerExample.class.getName());

    public KosarForm(RendelesService rendelesService){
        alapRendelesService = rendelesService;
        this.setAlignItems(Alignment.CENTER);
        add(fomenu);
        rendelesekTabla.setHeightByRows(true);
        rendelesekTabla.addColumn(RendelesiEgysegEntity::getGumi).setHeader("Gumi").setWidth("300px");
        rendelesekTabla.addColumn(RendelesiEgysegEntity::getMennyiseg).setHeader("Darab");
        rendelesekTabla.addColumn(RendelesiEgysegEntity::getReszosszeg).setHeader("Összeg");
        rendelesekTabla.setWidth("700px");
        vegosszeg.setSuffixComponent(new Span("Ft"));
        vegosszeg.setReadOnly(true);

        if(alapRendelesService.tokenreKeres(UI.getCurrent().getSession().getCsrfToken()) != null) {
            rendelesEntity = alapRendelesService.tokenreKeres(UI.getCurrent().getSession().getCsrfToken());
            LOGGER.info(rendelesEntity.toString());
            rendelesekTablaFeltolt(rendelesEntity.getRendelesiEgysegek());
            rendelesEntity.setVegosszeg(rendelesVegosszeg());
            vegosszeg.setValue(rendelesEntity.getVegosszeg().toString());
            tartalom.add(new VerticalLayout(rendelesekTabla, vegosszeg), new VerticalLayout(vevoAdatai, gombok));
            add(new HorizontalLayout(tartalom));
            megrendeles.addClickListener(e -> megrendeles());
        }
        if(KosarForm.vaneKosartartalom()){
            fomenu.getKosar().setIcon(new Icon(VaadinIcon.CART));
        }
        fomenu.getKosar().getStyle().set("color", "blue");
    }


    private Integer rendelesVegosszeg(){
        Integer osszeg = 0;
        for(RendelesiEgysegEntity rendeles : rendelesEntity.getRendelesiEgysegek()){
            osszeg += rendeles.getReszosszeg();
        }

        return osszeg;
    }

    private void rendelesekTablaFeltolt(List<RendelesiEgysegEntity> rendelesiEgysegek){
        rendelesekTabla.setItems(rendelesiEgysegek);
        rendelesekTabla.addColumn(new NativeButtonRenderer<>("Töröl", this::torlesMegrendelesbol));
        rendelesekTabla.getDataProvider().refreshAll();
    }

    private void torlesMegrendelesbol(RendelesiEgysegEntity rendeles){
        if(rendelesEntity.getRendelesiEgysegek().size()>1){
            rendelesEntity.getRendelesiEgysegek().remove(rendeles);
            int osszeg = 0;
            for(RendelesiEgysegEntity rendelesiEgysegEntity : rendelesEntity.getRendelesiEgysegek()){
                osszeg += rendelesiEgysegEntity.getReszosszeg();
            }
            rendelesEntity.setVegosszeg(osszeg);
            vegosszeg.setValue(rendelesEntity.getVegosszeg().toString());
            rendelesekTabla.getDataProvider().refreshAll();
        }else {
            alapRendelesService.torol(rendelesEntity);
            UI.getCurrent().navigate("gumik");
        }

    }

    private void megrendeles(){
        UgyfelEntity ugyfel = new UgyfelEntity();
        ugyfel.setNev(vevoAdatai.getNev().getValue());
        ugyfel.setEmail(vevoAdatai.getEmail().getValue());
        ugyfel.setTelefon(vevoAdatai.getTelefon().getValue());
        rendelesEntity.setUgyfel(ugyfel);
        rendelesEntity.setStatusz(RendelesStatusz.MEGRENDELVE);
        rendelesEntity.setDatum(LocalDate.now());
        String hiba = alapRendelesService.mentKosarbol(rendelesEntity);
        if(hiba != null){
            Notification hibaAblak = new HibaJelzes(hiba);
            hibaAblak.open();
        }else{
            UI.getCurrent().getSession().close();
            UI.getCurrent().navigate("gumik");
        }

    }

    public static boolean vaneKosartartalom(){
        try{
            LOGGER.info(alapRendelesService.tokenreKeres(UI.getCurrent().getSession().getCsrfToken()).toString());
            return true;
        }catch(NullPointerException e){
            return false;
        }
    }

}
