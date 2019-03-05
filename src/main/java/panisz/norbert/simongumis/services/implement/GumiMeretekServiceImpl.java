package panisz.norbert.simongumis.services.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import panisz.norbert.simongumis.entities.GumiMeretekEntity;
import panisz.norbert.simongumis.repositories.GumiMeretekRepository;
import panisz.norbert.simongumis.services.GumiMeretekService;
import java.util.List;

@Service
@Transactional
public class GumiMeretekServiceImpl implements GumiMeretekService {
    @Autowired
    GumiMeretekRepository gumiMeretekRepository;

    @Override
    public List<GumiMeretekEntity> szelessegreKeres(Integer szelesseg) {
        return gumiMeretekRepository.findAllBySzelesseg(szelesseg);
    }

    @Override
    public List<GumiMeretekEntity> profilraKeres(Integer profil) {
        return gumiMeretekRepository.findAllByProfil(profil);
    }

    @Override
    public List<GumiMeretekEntity> felnireKeres(Integer felni) {
        return gumiMeretekRepository.findAllByFelni(felni);
    }

    @Override
    public List<GumiMeretekEntity> szelessegreEsProfilraKeres(Integer szelesseg, Integer profil) {
        return gumiMeretekRepository.findAllBySzelessegAndProfil(szelesseg, profil);
    }

    @Override
    public List<GumiMeretekEntity> szelessegreEsFelnireKeres(Integer szelesseg, Integer felni) {
        return gumiMeretekRepository.findAllBySzelessegAndFelni(szelesseg, felni);
    }

    @Override
    public List<GumiMeretekEntity> profilraEsFelnireKeres(Integer profil, Integer felni) {
        return gumiMeretekRepository.findAllByProfilAndFelni(profil, felni);
    }

    @Override
    public GumiMeretekEntity mindenMeretreKeres(Integer szelesseg, Integer profil, Integer felni) {
        return gumiMeretekRepository.findBySzelessegAndProfilAndFelni(szelesseg, profil, felni);
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
}
