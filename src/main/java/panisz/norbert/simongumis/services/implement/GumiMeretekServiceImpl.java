package panisz.norbert.simongumis.services.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import panisz.norbert.simongumis.entities.GumiMeretekEntity;
import panisz.norbert.simongumis.repositories.GumiMeretekRepository;
import panisz.norbert.simongumis.repositories.GumikRepository;
import panisz.norbert.simongumis.services.GumiMeretekService;
import java.util.List;

@Service
@Transactional
public class GumiMeretekServiceImpl implements GumiMeretekService {
    @Autowired
    GumiMeretekRepository gumiMeretekRepository;

    @Autowired
    GumikRepository gumikRepository;

    @Override
    public List<GumiMeretekEntity> szelessegreKeresMenuhoz(Integer szelesseg) {
        return amihezVanGumi(gumiMeretekRepository.findAllBySzelesseg(szelesseg));
    }

    @Override
    public List<GumiMeretekEntity> profilraKeresMenuhoz(Integer profil) {
        return amihezVanGumi(gumiMeretekRepository.findAllByProfil(profil));
    }

    @Override
    public List<GumiMeretekEntity> felnireKeresMenuhoz(Integer felni) {
        return amihezVanGumi(gumiMeretekRepository.findAllByFelni(felni));
    }

    @Override
    public List<GumiMeretekEntity> szelessegreEsProfilraKeresMenuhoz(Integer szelesseg, Integer profil) {
        return amihezVanGumi(gumiMeretekRepository.findAllBySzelessegAndProfil(szelesseg, profil));
    }

    @Override
    public List<GumiMeretekEntity> szelessegreEsFelnireKeresMenuhoz(Integer szelesseg, Integer felni) {
        return amihezVanGumi(gumiMeretekRepository.findAllBySzelessegAndFelni(szelesseg, felni));
    }

    @Override
    public List<GumiMeretekEntity> profilraEsFelnireKeresMenuhoz(Integer profil, Integer felni) {
        return amihezVanGumi(gumiMeretekRepository.findAllByProfilAndFelni(profil, felni));
    }


    @Override
    public List<GumiMeretekEntity> osszes() {
        return gumiMeretekRepository.findAll();
    }

    @Override
    public GumiMeretekEntity ment(GumiMeretekEntity gumiMeretekEntity) {
        return gumiMeretekRepository.save(gumiMeretekEntity);
    }

    @Override
    public void torol(GumiMeretekEntity gumiMeretekEntity) {
        gumiMeretekRepository.delete(gumiMeretekEntity);
    }

    @Override
    public List<GumiMeretekEntity> osszesMenuhoz() {
        return amihezVanGumi(gumiMeretekRepository.findAll());
    }

    @Override
    public List<GumiMeretekEntity> amihezVanGumi(List<GumiMeretekEntity> gumiMeretek){
        for(GumiMeretekEntity gumiMeret : gumiMeretek){
            if(gumikRepository.findAllByMeretId(gumiMeret.getId()) == null){
                gumiMeretek.remove(gumiMeret);
            }
        }
        return gumiMeretek;
    }
}
