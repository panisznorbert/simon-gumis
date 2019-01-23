package panisz.norbert.simongumis.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import panisz.norbert.simongumis.entities.GumikEntity;
import panisz.norbert.simongumis.repositories.GumiMeretekRepository;
import panisz.norbert.simongumis.repositories.GumikRepository;

import java.util.ArrayList;


public class GumikView extends HorizontalLayout {

    private static GumikRepository alapGumikRepository = null;
    private static GumiMeretekRepository alapGumiMeretekRepository = null;
    Button alap = new Button();
    private VerticalLayout tartalom = new VerticalLayout();
    private GumiKeresoMenu menu;
    private Grid<GumikEntity> gumik = new Grid<>();

    public GumikView(GumikRepository gumikRepository, GumiMeretekRepository gumiMeretekRepository){
        alapGumikRepository = gumikRepository;
        alapGumiMeretekRepository = gumiMeretekRepository;
        init();
    }

    public void init(){
        menu = new GumiKeresoMenu(alapGumiMeretekRepository);
        tartalom.add(new HorizontalLayout(menu), new HorizontalLayout(gumik));
        menu.getKeres().addClickListener(e -> gumikTablaFeltolt(menu.getKriterium()));

        gumik.addColumn(GumikEntity::getGyarto).setHeader("Gyártó");
        gumik.addColumn(GumikEntity::getMeret).setHeader("Méret");
        gumik.addColumn(GumikEntity::getEvszak).setHeader("Évszak");
        gumik.addColumn(GumikEntity::getAllapot).setHeader("Állapot");
        gumik.addColumn(GumikEntity::getAr).setHeader("Ár");
        gumik.addColumn(GumikEntity::getMennyisegRaktarban).setHeader("Raktáron (db)");
        gumik.setWidth("800px");

        add(alap, tartalom);
    }

    public void gumikTablaFeltolt(GumikEntity szures){
        ArrayList<GumikEntity> all = new ArrayList<>();
        ArrayList<GumikEntity> szurtAdatok = new ArrayList<>();
        all.addAll(alapGumikRepository.findAll());
        szurtAdatok.addAll(all);
        int h = all.size();
        for(int i=0;i<h;i++){
            if(szures.getMeret().getSzelesseg() != 0 && all.get(i).getMeret().getSzelesseg() != szures.getMeret().getSzelesseg()){
                szurtAdatok.remove(all.get(i));
                continue;
            }
            if(szures.getMeret().getProfil() != 0 && all.get(i).getMeret().getProfil() != szures.getMeret().getProfil()){
                szurtAdatok.remove(all.get(i));
                continue;
            }
            if(szures.getMeret().getFelni() != 0 && all.get(i).getMeret().getFelni() != szures.getMeret().getFelni()){
                szurtAdatok.remove(all.get(i));
                continue;
            }
            if(szures.getEvszak() != "" && all.get(i).getEvszak() != szures.getEvszak()){
                szurtAdatok.remove(all.get(i));
                continue;
            }
            if(szures.getAllapot() != "" && all.get(i).getAllapot() != szures.getAllapot()){
                szurtAdatok.remove(all.get(i));
                continue;
            }
            if(szures.getGyarto() != "" && !all.get(i).getGyarto().equalsIgnoreCase(szures.getGyarto())){
                szurtAdatok.remove(all.get(i));
                continue;
            }
            if(all.get(i).getAr() < GumiKeresoMenu.getKezdoAr()){
                szurtAdatok.remove(all.get(i));
                continue;
            }
            if(GumiKeresoMenu.getVegAr() != 0 && all.get(i).getAr() > GumiKeresoMenu.getVegAr()){
                szurtAdatok.remove(all.get(i));
                continue;
            }
        }
        gumik.setItems(szurtAdatok);
        gumik.getDataProvider().refreshAll();
    }


}
