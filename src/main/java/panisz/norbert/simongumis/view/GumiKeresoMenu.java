package panisz.norbert.simongumis.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;


public class GumiKeresoMenu extends HorizontalLayout {

    private ComboBox meret1 = new ComboBox("Méret-szélesség");
    private ComboBox meret2 = new ComboBox("Méret-profil arány");
    private ComboBox meret3 = new ComboBox("Méret-felni átmérő");
    private ComboBox evszak = new ComboBox("Évszak", "Téli", "Nyári");
    private ComboBox allapot = new ComboBox("Állapot", "Új","Használt");


    private Button alaphelyzet = new Button("Alaphelyzet");
    private Button keres = new Button("Keresés");



    public GumiKeresoMenu(){
        init();
    }

    private void init(){
        add(meret1, meret2, meret3, evszak, allapot, alaphelyzet, keres);
    }
}
