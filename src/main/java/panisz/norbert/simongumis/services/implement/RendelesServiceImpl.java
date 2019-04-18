package panisz.norbert.simongumis.services.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import panisz.norbert.simongumis.LoggerExample;
import panisz.norbert.simongumis.components.HibaJelzes;
import panisz.norbert.simongumis.entities.GumikEntity;
import panisz.norbert.simongumis.entities.RendelesEntity;
import panisz.norbert.simongumis.entities.RendelesStatusz;
import panisz.norbert.simongumis.entities.RendelesiEgysegEntity;
import panisz.norbert.simongumis.repositories.GumikRepository;
import panisz.norbert.simongumis.repositories.RendelesRepository;
import panisz.norbert.simongumis.services.RendelesService;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@Service
@Transactional
public class RendelesServiceImpl implements RendelesService {
    @Autowired
    RendelesRepository rendelesRepository;
    @Autowired
    GumikRepository gumikRepository;

    private final static Logger LOGGER = Logger.getLogger(LoggerExample.class.getName());

    @Override
    public List<RendelesEntity> osszes() {
        return rendelesRepository.findAll();
    }

    @Override
    public RendelesEntity ment(RendelesEntity rendelesEntity) {
        return rendelesRepository.save(rendelesEntity);
    }

    @Override
    public String mentKosarbol(RendelesEntity rendelesEntity) {
        String hiba = null;
        //végső vizsgálat hogy a rendelésben szereplő gumikból a kívánt darabszám van-e raktáron
        for(RendelesiEgysegEntity rendelesiEgysegEntity : rendelesEntity.getRendelesiEgysegek()){
            GumikEntity gumikEntity = gumikRepository.findById(rendelesiEgysegEntity.getGumi().getId()).get();
            if(gumikEntity.getMennyisegRaktarban()<rendelesiEgysegEntity.getMennyiseg()){
                hiba = (rendelesiEgysegEntity.getGumi().toString() + " típusú gumiból, maximum " + gumikEntity.getMennyisegRaktarban().toString() + " db elérhető, de a rendelésében több szerepel!");
            }
        }
        //megrendelésnél a raktárkészlatből a gumik levonása
        if(hiba == null){
            for(RendelesiEgysegEntity rendelesiEgysegEntity : rendelesEntity.getRendelesiEgysegek()){
                GumikEntity gumikEntity = gumikRepository.findById(rendelesiEgysegEntity.getGumi().getId()).get();
                gumikEntity.setMennyisegRaktarban(gumikEntity.getMennyisegRaktarban()-rendelesiEgysegEntity.getMennyiseg());
                try{
                    gumikRepository.save(gumikEntity);
                }catch(Exception e){
                    hiba = "Mentés sikertelen";
                }
            }
            LOGGER.info(rendelesRepository.save(rendelesEntity).toString());
        }
        if(hiba == null){
            try{
                rendelesRepository.save(rendelesEntity);
            }catch(Exception e){
                hiba = "Mentés sikertelen";
            }
        }

        return hiba;
    }

    @Override
    public void torol(RendelesEntity rendelesEntity) {
        rendelesRepository.delete(rendelesEntity);
    }

    @Override
    public void rendelesTrolese(RendelesEntity rendelesEntity){
        for(RendelesiEgysegEntity rendelesiEgysegEntity : rendelesEntity.getRendelesiEgysegek()){
            GumikEntity gumikEntity = gumikRepository.findById(rendelesiEgysegEntity.getGumi().getId()).get();
            gumikEntity.setMennyisegRaktarban(gumikEntity.getMennyisegRaktarban()+rendelesiEgysegEntity.getMennyiseg());
            gumikRepository.save(gumikEntity);
        }
        rendelesRepository.save(rendelesEntity);
    }

    @Override
    public RendelesEntity idKereses(Integer id){
        return rendelesRepository.findById(id).get();
    }

    @Override
    public RendelesEntity tokenreKeres(String token){
        return rendelesRepository.findByToken(token);
    }

    @Override
    public List<RendelesEntity> ugyfelNevreKeres(String nev){return rendelesRepository.findAllByUgyfel_Nev(nev);}

    @Override
    public List<RendelesEntity> rendelesekreKeres(RendelesStatusz statusz){return rendelesRepository.findAllByStatuszIsNot(statusz);}

}
