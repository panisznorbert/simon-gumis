package panisz.norbert.simongumis.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import javafx.beans.property.SimpleSetProperty;
import panisz.norbert.simongumis.entities.GumiMeretekEntity;
import panisz.norbert.simongumis.repositories.GumiMeretekRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class GumiKeresoMenu extends HorizontalLayout {
    private static GumiMeretekRepository alapGumiMeretekRepository = null;
    private ComboBox meret1 = new ComboBox("Méret-szélesség");
    private ComboBox meret2 = new ComboBox("Méret-profil arány");
    private ComboBox meret3 = new ComboBox("Méret-felni átmérő");

    private ComboBox evszak = new ComboBox("Évszak", "Téli", "Nyári");
    private ComboBox allapot = new ComboBox("Állapot", "Új","Használt");

    private TextField gyarto = new TextField("Gyártó");
    private TextField artol = new TextField("Ár -tól");
    private TextField arig = new TextField("Ár -ig");

    private Button egyeb = new Button("+ feltételek");
    private Button alaphelyzet = new Button("Alaphelyzet");
    private Button keres = new Button("Keresés");


    private HorizontalLayout menu1 = new HorizontalLayout();
    private HorizontalLayout menu2 = new HorizontalLayout();
    private VerticalLayout menu = new VerticalLayout();


    public GumiKeresoMenu(GumiMeretekRepository gumiMeretekRepository){
        alapGumiMeretekRepository = gumiMeretekRepository;
        init();
        egyeb.addClickListener(e -> tovabbiFeltetelek());
        alaphelyzet.addClickListener(e -> init());
    }

    private void init(){
        List<GumiMeretekEntity> gumiMeretek1 = alapGumiMeretekRepository.findAll();
        ArrayList<GumiMeretekEntity> gumiMeretek2 = new ArrayList<>();
        gumiMeretek2.addAll(gumiMeretek1);
        Set<Integer> meretSet1 = new HashSet<>();
        Set<Integer> meretSet2 = new HashSet<>();
        Set<Integer> meretSet3 = new HashSet<>();
        for(int i=0;i<gumiMeretek2.size();i++){
            meretSet1.add(gumiMeretek2.get(i).getSzelesseg());
            meretSet2.add(gumiMeretek2.get(i).getProfil());
            meretSet3.add(gumiMeretek2.get(i).getFelni());
        }

        meret1.setItems(meretSet1);
        meret2.setItems(meretSet2);
        meret3.setItems(meretSet3);
        evszak.clear();
        allapot.clear();
        gyarto.clear();
        artol.clear();
        arig.clear();
        menu1.removeAll();
        menu1.add(meret1, meret2, meret3, evszak, allapot);
        menu2.removeAll();
        menu2.add(egyeb, alaphelyzet, keres);
        menu.removeAll();
        menu.add(menu1, menu2);
        menu2.setAlignItems(Alignment.END);
        add(menu);
    }

    private void tovabbiFeltetelek(){
        menu2.removeAll();
        menu2.add(gyarto, artol, arig, alaphelyzet, keres);
    }
}
