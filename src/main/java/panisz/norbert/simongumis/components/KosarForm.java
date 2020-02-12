package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;
import panisz.norbert.simongumis.entities.*;
import panisz.norbert.simongumis.exceptions.HibasKitoltesException;
import panisz.norbert.simongumis.services.RendelesService;
import java.time.LocalDate;

@StyleSheet("kosar_idopontfoglalas.css")
@UIScope
@Component
public class KosarForm extends VerticalLayout {

    private static RendelesService alapRendelesService;

    private RendelesEntity rendelesEntity;
    private UgyfelMezok vevoAdatai = new UgyfelMezok();
    private VerticalLayout vegosszeg = new VerticalLayout();
    private VerticalLayout rendelesek = new VerticalLayout();

    public KosarForm(RendelesService rendelesService){
        alapRendelesService = rendelesService;

        if(alapRendelesService.sessionreKeres(UI.getCurrent().getSession().getSession().getId()) != null) {
            this.setId("kosar_idopontfoglalas_alap");
            rendelesEntity = alapRendelesService.sessionreKeres(UI.getCurrent().getSession().getSession().getId());
            rendelesekBetoltese();
            rendelesEntity.setVegosszeg(rendelesVegosszeg());
            VerticalLayout tartalom = new VerticalLayout();
            HorizontalLayout megrendelesSorok = new HorizontalLayout();
            Button megrendeles = new Button("Megrendelés");
            megrendeles.setId("gomb");
            HorizontalLayout gombok = new HorizontalLayout(megrendeles);
            tartalom.add(megrendelesSorok, new VerticalLayout(vevoAdatai, gombok));
            add(tartalom);
            megrendeles.addClickListener(e -> megrendeles());
        }
    }

    private Integer rendelesVegosszeg(){
        Integer osszeg = 0;
        for(RendelesiEgysegEntity rendeles : rendelesEntity.getRendelesiEgysegek()){
            osszeg += rendeles.getReszosszeg();
        }
        return osszeg;
    }

    private void rendelesekBetoltese(){
        rendelesek.setId("rendelesek");
        for(RendelesiEgysegEntity rendelesiEgysegEntity: rendelesEntity.getRendelesiEgysegek()){
            rendelesek.add(MegrendelesSorok(rendelesiEgysegEntity));
        }
        rendelesEntity.setVegosszeg(rendelesVegosszeg());
        vegosszeg.add(new Label("Végösszeg: " + rendelesEntity.getVegosszeg().toString() + " Ft"));
        vegosszeg.setId("vegosszeg");
        vegosszeg.setSizeUndefined();
        this.setSizeUndefined();
        this.add(rendelesek, vegosszeg);
    }

    private HorizontalLayout MegrendelesSorok(RendelesiEgysegEntity rendelesiEgysegEntity){
        HorizontalLayout ujSor = new HorizontalLayout();

        Label reszosszeg = new Label(rendelesiEgysegEntity.getReszosszeg().toString() + " Ft");
        reszosszeg.setId("reszosszeg-label");
        Label gumi = new Label(rendelesiEgysegEntity.getGumi().toString());
        gumi.setId("gumi-megnevezes-label");

        Integer darabSzam = alapRendelesService.gumiDarabszamRaktaron(rendelesiEgysegEntity);
        Label darabszamHiba = new Label("maximum " + darabSzam + " db");
        darabszamHiba.setId("darabszamHiba-label");
        darabszamHiba.setVisible(false);

        TextField darab = new TextField();
        darab.setPattern("[0-9]*");
        darab.setId("darab-mezo");
        darab.setValue(rendelesiEgysegEntity.getMennyiseg().toString());
        Button csokkent = new Button("-");
        csokkent.setId("csokkent");
        csokkent.addClickListener(e -> rendelesCsokkentese(darab, darabszamHiba, rendelesiEgysegEntity, reszosszeg));
        Button novel = new Button("+");
        novel.setId("novel");
        novel.addClickListener(e -> rendelesNovelese(darab, alapRendelesService.gumiDarabszamRaktaron(rendelesiEgysegEntity), darabszamHiba, rendelesiEgysegEntity, reszosszeg));
        HorizontalLayout darabszamMegrendeles = new HorizontalLayout(csokkent, darab, novel);

        Icon torol = new Icon(VaadinIcon.TRASH);
        torol.setId("torles-ikon");
        torol.addClickListener(e -> torlesMegrendelesbol(rendelesiEgysegEntity, ujSor));
        VerticalLayout darabszamos = new VerticalLayout(darabszamMegrendeles, darabszamHiba);
        darabszamos.setId("darabszamos");
        darabszamos.setSizeUndefined();
        ujSor.add(gumi, darabszamos, reszosszeg, torol);
        ujSor.setId("rendeles-sorok");
        ujSor.setSizeUndefined();
        return ujSor;
    }

    private void rendelesCsokkentese(TextField mezo, Label hiba, RendelesiEgysegEntity rendelesiEgysegEntity, Label reszosszeg){
        if(hiba.isVisible()){
            hiba.setVisible(false);
        }
        if(!mezo.getValue().equals("0")){
            int adat = Integer.parseInt(mezo.getValue())-1;
            for(RendelesiEgysegEntity rendelesiEgyseg : rendelesEntity.getRendelesiEgysegek()){
                if(rendelesiEgyseg.getId().equals(rendelesiEgysegEntity.getId())){
                    rendelesiEgyseg.setMennyiseg(rendelesiEgyseg.getMennyiseg()-1);
                    rendelesiEgyseg.setReszosszeg(rendelesiEgysegEntity.getReszosszeg()-rendelesiEgysegEntity.getGumi().getAr());
                    reszosszeg.setText(rendelesiEgyseg.getReszosszeg().toString() + " Ft");
                }
            }
            mezo.setValue(Integer.toString(adat));
            darabszamModositasMentes();
        }
    }

    private void rendelesNovelese(TextField mezo, int max, Label hiba, RendelesiEgysegEntity rendelesiEgysegEntity, Label reszosszeg){
        hiba.setVisible(false);
        if(!mezo.getValue().equals(Integer.toString(max))){
            int adat = Integer.parseInt(mezo.getValue())+1;
            for(RendelesiEgysegEntity rendelesiEgyseg : rendelesEntity.getRendelesiEgysegek()){
                if(rendelesiEgyseg.getId().equals(rendelesiEgysegEntity.getId())){
                    rendelesiEgyseg.setMennyiseg(rendelesiEgyseg.getMennyiseg()+1);
                    rendelesiEgyseg.setReszosszeg(rendelesiEgysegEntity.getReszosszeg()+rendelesiEgysegEntity.getGumi().getAr());
                    reszosszeg.setText(rendelesiEgyseg.getReszosszeg().toString() + " Ft");
                }
            }
            mezo.setValue(Integer.toString(adat));
            darabszamModositasMentes();
        }else{
            hiba.setVisible(true);
        }
    }

    private void darabszamModositasMentes(){
        try {
            rendelesEntity.setVegosszeg(rendelesVegosszeg());
            alapRendelesService.ment(rendelesEntity);
        } catch (Exception e) {
            Hibajelzes hiba = new Hibajelzes("A módosítás sikertelen, próbálja újra");
            hiba.open();
        }
        vegosszeg.removeAll();
        vegosszeg.add(new Label("Végösszeg: " + rendelesEntity.getVegosszeg().toString() + " Ft"));
    }

    private void torlesMegrendelesbol(RendelesiEgysegEntity rendeles, HorizontalLayout sor){
        if(rendelesEntity.getRendelesiEgysegek().size()>1){
            rendelesEntity.getRendelesiEgysegek().remove(rendeles);
            int osszeg = 0;
            for(RendelesiEgysegEntity rendelesiEgysegEntity : rendelesEntity.getRendelesiEgysegek()){
                osszeg += rendelesiEgysegEntity.getReszosszeg();
            }
            rendelesEntity.setVegosszeg(osszeg);
            try {
                alapRendelesService.ment(rendelesEntity);
                rendelesek.remove(sor);
                vegosszeg.removeAll();
                vegosszeg.add(new Label("Végösszeg: " + rendelesEntity.getVegosszeg().toString() + "Ft"));
            } catch (Exception e) {
                Hibajelzes hiba = new Hibajelzes("A törlés sikertelen, próbálja újra");
                hiba.open();
            }
        }else {
            alapRendelesService.torol(rendelesEntity);
            UI.getCurrent().navigate("gumik");
        }
    }

    private void megrendeles(){
        try{
            vevoAdatai.kitoltottseg();
            UgyfelEntity ugyfel = new UgyfelEntity();
            ugyfel.setNev(vevoAdatai.getNev().getValue());
            ugyfel.setEmail(vevoAdatai.getEmail().getValue());
            ugyfel.setTelefon(vevoAdatai.getTelefon().getValue());
            rendelesEntity.setUgyfel(ugyfel);
            rendelesEntity.setStatusz(RendelesStatusz.MEGRENDELVE);
            rendelesEntity.setDatum(LocalDate.now());
            rendelesEntity.getRendelesiEgysegek().removeIf(rendelesiEgysegEntity -> rendelesiEgysegEntity.getMennyiseg().equals(0));
            String hiba = alapRendelesService.mentKosarbol(rendelesEntity);
            if(hiba != null){
                Notification hibaAblak = new Hibajelzes(hiba);
                hibaAblak.open();
            }else{
                UI.getCurrent().navigate("gumik");
            }
        }catch(HibasKitoltesException ex){
            Notification hibaAblak = new Hibajelzes(ex.getMessage());
            hibaAblak.open();
        }
    }

}
