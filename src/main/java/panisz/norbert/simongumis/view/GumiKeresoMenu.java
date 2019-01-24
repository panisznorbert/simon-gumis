package panisz.norbert.simongumis.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import panisz.norbert.simongumis.entities.GumiMeretekEntity;
import panisz.norbert.simongumis.entities.GumikEntity;
import panisz.norbert.simongumis.repositories.GumiMeretekRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class GumiKeresoMenu extends HorizontalLayout {
    private static GumiMeretekRepository alapGumiMeretekRepository = null;
    private GumikEntity kriterium = new GumikEntity();
    private static Integer kezdoAr;
    private static Integer vegAr;

    private ComboBox<Integer> meret1 = new ComboBox("Méret-szélesség");
    private ComboBox<Integer> meret2 = new ComboBox("Méret-profil arány");
    private ComboBox<Integer> meret3 = new ComboBox("Méret-felni átmérő");
    private HorizontalLayout meret1cb = new HorizontalLayout(meret1);
    private HorizontalLayout meret2cb = new HorizontalLayout(meret2);
    private HorizontalLayout meret3cb = new HorizontalLayout(meret3);

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
        keres.addClickListener(e -> adatokBeallitasa());

    }

    private void adatokBeallitasa(){
        if(gyarto.isEmpty()){
            kriterium.setGyarto("");
        }else{
            kriterium.setGyarto(gyarto.getValue());
        }

        if(artol.isEmpty()){
            kezdoAr=0;
        }else{
            kezdoAr=Integer.valueOf(artol.getValue());
        }

        if(arig.isEmpty()){
            vegAr=0;
        }else{
            vegAr=Integer.valueOf(arig.getValue());
        }

        if(evszak.isEmpty()){
            kriterium.setEvszak("");
        }else{
            kriterium.setEvszak(evszak.getValue().toString());
        }

        if(allapot.isEmpty()){
            kriterium.setAllapot("");
        }else{
            kriterium.setAllapot(allapot.getValue().toString());
        }
    }


    private void szelessegKivalasztas(){
        if(meret1.isEmpty()){
            if(meret2.isEmpty()){
                if(meret3.isEmpty()){
                    //0, 0, 0
                    //evszak.setLabel("000");
                    meretFeltolto(alapGumiMeretekRepository.findAll(), 0, 0, 0);
                    setMeret(0, 0, 0);
                }else{
                    //0, 0, 1
                    //evszak.setLabel("001");
                    meretFeltolto(alapGumiMeretekRepository.findAllByFelni(Integer.valueOf(meret3.getValue())), 0, 0, 1);
                    setMeret(0, 0, Integer.valueOf(meret3.getValue()));
                }
            }else{
                if(meret3.isEmpty()){
                    //0, 1, 0
                    //evszak.setLabel("010");
                    meretFeltolto(alapGumiMeretekRepository.findAllByProfil(Integer.valueOf(meret2.getValue())),0 ,1 ,0);
                    setMeret(0, Integer.valueOf(meret2.getValue()), 0);
                }else{
                    //0, 1, 1
                    //evszak.setLabel("011");
                    meretFeltolto(alapGumiMeretekRepository.findAllByProfilAndFelni(Integer.valueOf(meret2.getValue()), Integer.valueOf(meret3.getValue())),0,1,1);
                    setMeret(0, Integer.valueOf(meret2.getValue()), Integer.valueOf(meret3.getValue()));
                }
            }
        }else{
            if(meret2.isEmpty()){
                if(meret3.isEmpty()){
                    //1, 0, 0
                    //evszak.setLabel("100");
                    meretFeltolto(alapGumiMeretekRepository.findAllBySzelesseg(Integer.valueOf(meret1.getValue())), 1, 0, 0);
                    setMeret(Integer.valueOf(meret1.getValue()), 0, 0);
                }else{
                    //1, 0, 1
                    //evszak.setLabel("101");
                    meretFeltolto(alapGumiMeretekRepository.findAllBySzelessegAndFelni(Integer.valueOf(meret1.getValue()), Integer.valueOf(meret3.getValue())), 1, 0, 1);
                    setMeret(Integer.valueOf(meret1.getValue()), 0, Integer.valueOf(meret3.getValue()));
                }
            }else{
                if(meret3.isEmpty()){
                    //1, 1, 0
                    //evszak.setLabel("110");
                    meretFeltolto(alapGumiMeretekRepository.findAllBySzelessegAndProfil(Integer.valueOf(meret1.getValue()), Integer.valueOf(meret2.getValue())), 1, 1, 0);
                    setMeret(Integer.valueOf(meret1.getValue()), Integer.valueOf(meret2.getValue()), 0);
                }else{
                    setMeret(Integer.valueOf(meret1.getValue()), Integer.valueOf(meret2.getValue()), Integer.valueOf(meret3.getValue()));
                }
            }
        }

    }

    private void setMeret(Integer a, Integer b, Integer c){
        kriterium.getMeret().setSzelesseg(a);
        kriterium.getMeret().setProfil(b);
        kriterium.getMeret().setFelni(c);
    }

    private void meretFeltolto(List<GumiMeretekEntity> gumiMeretekEntities, int a, int b, int c){
        ArrayList<GumiMeretekEntity> gumiMeretek = new ArrayList<>();
        gumiMeretek.addAll(gumiMeretekEntities);
        Set<Integer> meretSet1 = new HashSet<>();
        Set<Integer> meretSet2 = new HashSet<>();
        Set<Integer> meretSet3 = new HashSet<>();
        for(int i=0;i<gumiMeretek.size();i++){
            meretSet1.add(gumiMeretek.get(i).getSzelesseg());
            meretSet2.add(gumiMeretek.get(i).getProfil());
            meretSet3.add(gumiMeretek.get(i).getFelni());
        }

        meret1cb.remove(meret1);meret1 = new ComboBox("Méret-szélesség");meret1.setItems(meretSet1);meret1cb.add(meret1);
        meret2cb.remove(meret2);meret2 = new ComboBox("Méret-profil arány");meret2.setItems(meretSet2);meret2cb.add(meret2);
        meret3cb.remove(meret3);meret3 = new ComboBox("Méret-felni átmérő");meret3.setItems(meretSet3);meret3cb.add(meret3);

        if(a==1){meret1.setValue(gumiMeretek.get(0).getSzelesseg());}
        if(b==1){meret2.setValue(gumiMeretek.get(0).getProfil());}
        if(c==1){meret3.setValue(gumiMeretek.get(0).getFelni());}

        meret1.addValueChangeListener(e -> szelessegKivalasztas());
        meret2.addValueChangeListener(e -> szelessegKivalasztas());
        meret3.addValueChangeListener(e -> szelessegKivalasztas());
    }


    private void init(){
        meretFeltolto(alapGumiMeretekRepository.findAll(), 0, 0, 0);
        kriterium.setMeret(new GumiMeretekEntity());
        setMeret(0,0,0);
        kriterium.setGyarto("");
        kriterium.setAllapot("");
        kriterium.setEvszak("");
        kriterium.setMennyisegRaktarban(0);
        kriterium.setAr(Integer.valueOf(0));
        evszak.clear();
        allapot.clear();
        gyarto.clear();
        artol.clear();
        arig.clear();
        menu1.removeAll();
        menu1.add(meret1cb, meret2cb, meret3cb, evszak, allapot);
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


    public Button getKeres() {
        return keres;
    }

    public GumikEntity getKriterium() {
        return kriterium;
    }

    public static Integer getKezdoAr() {
        return kezdoAr;
    }

    public static Integer getVegAr() {
        return vegAr;
    }
}
