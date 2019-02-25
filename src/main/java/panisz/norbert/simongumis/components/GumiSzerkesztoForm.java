package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import panisz.norbert.simongumis.entities.GumiMeretekEntity;
import panisz.norbert.simongumis.entities.GumikEntity;
import panisz.norbert.simongumis.repositories.GumiMeretekRepository;


public class GumiSzerkesztoForm extends VerticalLayout {

    private TextField gyarto = new TextField("Gyártó");
    private TextField meret1 = new TextField("Méret-szélesség");
    private TextField meret2 = new TextField("Méret-profil arány");
    private TextField meret3 = new TextField("Méret-felni átmérő");
    private TextField ar = new TextField("Ár");
    private ComboBox evszak = new ComboBox("Évszak", "Téli", "Nyári");
    private ComboBox allapot = new ComboBox("Állapot", "Új","Használt");
    private TextField darab  = new TextField("Raktárkészlet");


    public GumiSzerkesztoForm(GumikEntity gumikEntity){

        ar.setPattern("\\d*(\\.\\d*)?");
        ar.setPreventInvalidInput(true);
        ar.setSuffixComponent(new Span("Ft"));

        darab.setPattern("[0-9]*");
        darab.setPreventInvalidInput(true);
        darab.setSuffixComponent(new Span("Db"));

        gyarto.setRequired(true);
        ar.setRequired(true);
        meret1.setRequired(true);
        meret2.setRequired(true);
        meret3.setRequired(true);
        evszak.setRequired(true);
        allapot.setRequired(true);
        darab.setRequired(true);

        gyarto.setValue(gumikEntity.getGyarto());
        meret1.setValue(gumikEntity.getMeret().getSzelesseg().toString());
        meret2.setValue(gumikEntity.getMeret().getProfil().toString());
        meret3.setValue(gumikEntity.getMeret().getFelni().toString());
        evszak.setValue(gumikEntity.getEvszak());
        allapot.setValue(gumikEntity.getAllapot());
        ar.setValue(gumikEntity.getAr().toString());
        darab.setValue(gumikEntity.getMennyisegRaktarban().toString());
        add(new HorizontalLayout(gyarto, meret1), new HorizontalLayout(meret2, meret3), new HorizontalLayout(evszak, allapot), new HorizontalLayout(ar, darab));
    }



    public GumikEntity beallit(GumikEntity gumikEntity, GumiMeretekRepository gumiMeretekRepository){
            gumikEntity.setGyarto(gyarto.getValue());
            GumiMeretekEntity meret = gumikEntity.getMeret();
            meret.setSzelesseg(Integer.valueOf(meret1.getValue()));
            meret.setProfil(Integer.valueOf(meret2.getValue()));
            meret.setFelni(Integer.valueOf(meret3.getValue()));
            gumiMeretekRepository.save(meret);
            gumikEntity.setMeret(meret);
            gumikEntity.setEvszak(evszak.getValue().toString());
            gumikEntity.setAllapot(allapot.getValue().toString());
            gumikEntity.setAr(Integer.valueOf(ar.getValue()));
            gumikEntity.setMennyisegRaktarban(Integer.valueOf(darab.getValue()));

        return gumikEntity;
    }

    public String validacio() {
        return GumikKezeleseForm.getString(gyarto, evszak, allapot, ar, darab, meret1, meret2, meret3);
    }




}
