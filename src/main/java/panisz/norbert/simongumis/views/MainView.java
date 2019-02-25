package panisz.norbert.simongumis.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import panisz.norbert.simongumis.components.*;
import panisz.norbert.simongumis.entities.RendelesiEgysegEntity;

import javax.annotation.PostConstruct;
import java.util.List;

@Route
public class MainView extends VerticalLayout{

    private static MenuForm fomenu = new MenuForm();

    private static VerticalLayout tartalom = new VerticalLayout();

    private static VerticalLayout layout = new VerticalLayout(fomenu, tartalom);


    @PostConstruct
    public void init() {
        add(layout);
    }


    public static void kosarhozAd(RendelesiEgysegEntity rendelesiEgysegEntity){
        Boolean benneVolt = false;
        for(RendelesiEgysegEntity aktualisRendelesiEgyseg:fomenu.getAktualisRendeles().getRendelesiEgysegek()){

            if(aktualisRendelesiEgyseg.getGumi().equals(rendelesiEgysegEntity.getGumi())){
                fomenu.getAktualisRendeles().getRendelesiEgysegek().get(fomenu.getAktualisRendeles().getRendelesiEgysegek().indexOf(aktualisRendelesiEgyseg))
                        .setMennyiseg(
                                fomenu.getAktualisRendeles().getRendelesiEgysegek().get(fomenu.getAktualisRendeles().getRendelesiEgysegek().indexOf(aktualisRendelesiEgyseg)).getMennyiseg()+rendelesiEgysegEntity.getMennyiseg()
                        );
                fomenu.getAktualisRendeles().getRendelesiEgysegek().get(fomenu.getAktualisRendeles().getRendelesiEgysegek().indexOf(aktualisRendelesiEgyseg))
                        .setReszosszeg(
                                fomenu.getAktualisRendeles().getRendelesiEgysegek().get(fomenu.getAktualisRendeles().getRendelesiEgysegek().indexOf(aktualisRendelesiEgyseg)).getReszosszeg()+rendelesiEgysegEntity.getReszosszeg()
                        );
                benneVolt = true;
            }
        }
        if(!benneVolt){
            fomenu.getAktualisRendeles().getRendelesiEgysegek().add(rendelesiEgysegEntity);

        }
        fomenu.getAktualisRendeles().setVegosszeg(vegosszegSzamol(fomenu.getAktualisRendeles().getRendelesiEgysegek()));
    }

    private static Integer vegosszegSzamol(List<RendelesiEgysegEntity> rendelesiEgysegek){
        Integer ossz = 0;
        for (RendelesiEgysegEntity rendelesiEgysegEntity:rendelesiEgysegek) {
            ossz += rendelesiEgysegEntity.getReszosszeg();
        }
        return ossz;
    }

    public static MenuForm getFomenu() {
        return fomenu;
    }

}
