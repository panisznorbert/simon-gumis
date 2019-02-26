package panisz.norbert.simongumis;

import org.springframework.beans.factory.annotation.Autowired;
import panisz.norbert.simongumis.entities.GumiMeretekEntity;
import panisz.norbert.simongumis.entities.GumikEntity;
import panisz.norbert.simongumis.repositories.GumikRepository;

import javax.annotation.PostConstruct;


public class TestAdatok {
    @Autowired
    GumikRepository gumikRepository;


    @PostConstruct
    private void alapFeltoltes(){
        ujGumi("Gyártó1", 155, 40, 20, 5000, "Téli", "Új", 20);
        ujGumi("Gyártó2", 155, 50, 18, 5500, "Téli", "Új", 2);
        ujGumi("Gyártó3", 205, 60, 20, 4000, "Téli", "Új", 4);
        ujGumi("Gyártó4", 155, 70, 19, 2000, "Téli", "Használt", 8);
    }

    private void ujGumi(String gyarto, Integer szelesseg, Integer profil, Integer felni, Integer ar, String evszak, String allapot, Integer darab){
        GumiMeretekEntity gumiMeretekEntity = new GumiMeretekEntity();
        gumiMeretekEntity.setSzelesseg(szelesseg);
        gumiMeretekEntity.setProfil(profil);
        gumiMeretekEntity.setFelni(felni);
        GumikEntity gumikEntity = new GumikEntity();
        gumikEntity.setGyarto(gyarto);
        gumikEntity.setMeret(gumiMeretekEntity);
        gumikEntity.setAllapot(allapot);
        gumikEntity.setEvszak(evszak);
        gumikEntity.setAr(ar);
        gumikEntity.setMennyisegRaktarban(darab);

        gumikRepository.save(gumikEntity);

    }
}
