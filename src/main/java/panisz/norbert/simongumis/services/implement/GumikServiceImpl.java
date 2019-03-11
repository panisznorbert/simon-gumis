package panisz.norbert.simongumis.services.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import panisz.norbert.simongumis.entities.GumikEntity;
import panisz.norbert.simongumis.repositories.GumikRepository;
import panisz.norbert.simongumis.services.GumikService;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class GumikServiceImpl implements GumikService {
    @Autowired
    GumikRepository gumikRepository;

    @Override
    public GumikEntity vanMarIlyen(String gyarto, Integer szelesseg, Integer profil, Integer felni, String evszak, String allapot) {
        return gumikRepository.findByGyartoAndMeret_SzelessegAndMeret_ProfilAndMeret_FelniAndEvszakAndAllapot(gyarto, szelesseg, profil, felni, evszak, allapot);
    }

    @Override
    public List<GumikEntity> osszes() {
        return gumikRepository.findAll();
    }

    @Override
    public GumikEntity ment(GumikEntity gumikEntity) {
        return gumikRepository.save(gumikEntity);
    }

    @Override
    public void torol(GumikEntity gumikEntity) {
        gumikRepository.delete(gumikEntity);
    }

    public GumikEntity idKereses(Integer id){
        return gumikRepository.findById(id).get();
    }

    public void torolMind(Set<GumikEntity> gumikEntities){
        gumikRepository.deleteAll(gumikEntities);
    }
}
