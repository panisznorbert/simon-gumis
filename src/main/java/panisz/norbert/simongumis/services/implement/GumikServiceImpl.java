package panisz.norbert.simongumis.services.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import panisz.norbert.simongumis.entities.GumiMeretekEntity;
import panisz.norbert.simongumis.entities.GumikEntity;
import panisz.norbert.simongumis.exceptions.LetezoGumiException;
import panisz.norbert.simongumis.repositories.GumiMeretekRepository;
import panisz.norbert.simongumis.repositories.GumikRepository;
import panisz.norbert.simongumis.services.GumikService;
import java.util.List;

@Service
@Transactional
public class GumikServiceImpl implements GumikService {
    @Autowired
    private GumikRepository gumikRepository;

    @Autowired
    private GumiMeretekRepository gumiMeretekRepository;

    @Override
    public List<GumikEntity> osszes() {
        return gumikRepository.findAll();
    }

    @Override
    public GumikEntity ment(GumikEntity gumikEntity) throws LetezoGumiException{

        //vizsgálni hogy van-e már ilyen méret lementve és ha igen ne mentsunk még egyet le
        GumiMeretekEntity meret = gumiMeretekRepository.findBySzelessegAndProfilAndFelni(
                gumikEntity.getMeret().getSzelesseg(),
                gumikEntity.getMeret().getProfil(),
                gumikEntity.getMeret().getFelni());
        if(meret != null){
            gumikEntity.setMeret(meret);
        }
        //vizsgálni, hogy van-e már ilyen gumi lementve, és ha igen akkor ne mentsunk még egyet le
        //módosítás esetén vizsgálva van hogy a módosítandón kívül van-e már ilyen
        GumikEntity mentettGumi = gumikRepository.findByGyartoAndMeret_SzelessegAndMeret_ProfilAndMeret_FelniAndEvszakAndAllapot(
                gumikEntity.getGyarto(),
                gumikEntity.getMeret().getSzelesseg(),
                gumikEntity.getMeret().getProfil(),
                gumikEntity.getMeret().getFelni(),
                gumikEntity.getEvszak(),
                gumikEntity.getAllapot()
        );

        if( mentettGumi != null && !mentettGumi.getId().equals(gumikEntity.getId())) {
            throw new LetezoGumiException(mentettGumi.getId());
        }

        return gumikRepository.save(gumikEntity);

    }

    @Override
    public void torol(GumikEntity gumikEntity) {
        gumikRepository.delete(gumikEntity);
    }

    @Override
    public GumikEntity idraKereses(Integer id) {
        return gumikRepository.findGumikEntityById(id);
    }
}
