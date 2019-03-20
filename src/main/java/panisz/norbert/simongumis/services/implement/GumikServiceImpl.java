package panisz.norbert.simongumis.services.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import panisz.norbert.simongumis.entities.GumiMeretekEntity;
import panisz.norbert.simongumis.entities.GumikEntity;
import panisz.norbert.simongumis.repositories.GumiMeretekRepository;
import panisz.norbert.simongumis.repositories.GumikRepository;
import panisz.norbert.simongumis.services.GumikService;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class GumikServiceImpl implements GumikService {
    @Autowired
    GumikRepository gumikRepository;

    @Autowired
    GumiMeretekRepository gumiMeretekRepository;

    @Override
    public GumikEntity vanMarIlyen(String gyarto, Integer szelesseg, Integer profil, Integer felni, String evszak, String allapot) {
        return gumikRepository.findByGyartoAndMeret_SzelessegAndMeret_ProfilAndMeret_FelniAndEvszakAndAllapot(gyarto, szelesseg, profil, felni, evszak, allapot);
    }

    @Override
    public List<GumikEntity> keresesMeretId(Integer id) {
        return gumikRepository.findAllByMeretId(id);
    }

    @Override
    public List<GumikEntity> osszes() {
        return gumikRepository.findAll();
    }

    @Override
    public String ment(GumikEntity gumikEntity) {
        String hiba = null;
        TransactionAspectSupport.currentTransactionStatus();


        //vizsgálni hogy van-e már ilyen méret lementve és ha igen ne mentsunk még egyet le
        GumiMeretekEntity meret = gumiMeretekRepository.findBySzelessegAndProfilAndFelni(
                gumikEntity.getMeret().getSzelesseg(),
                gumikEntity.getMeret().getProfil(),
                gumikEntity.getMeret().getFelni());
        if(meret != null){
            gumikEntity.setMeret(meret);
        }
        //vizsgálni, hogy van-e már ilyen gumi lementve, és ha igen akkor ne mentsunk még egyet le
        GumikEntity mentettGumi = gumikRepository.findByGyartoAndMeret_SzelessegAndMeret_ProfilAndMeret_FelniAndEvszakAndAllapot(
                gumikEntity.getGyarto(),
                gumikEntity.getMeret().getSzelesseg(),
                gumikEntity.getMeret().getProfil(),
                gumikEntity.getMeret().getFelni(),
                gumikEntity.getEvszak(),
                gumikEntity.getAllapot());
        if((mentettGumi != null) && (gumikEntity.getId() != mentettGumi.getId())){
            hiba = "Már van ilyen gumi";
        }else {
            try{
                gumikRepository.save(gumikEntity);
            }catch(Exception e){
                hiba = "Mentés sikertelen";
            }

        }

        return hiba;
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
