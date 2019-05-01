package panisz.norbert.simongumis.services.implement;

import com.vaadin.flow.component.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import panisz.norbert.simongumis.entities.GumikEntity;
import panisz.norbert.simongumis.entities.RendelesEntity;
import panisz.norbert.simongumis.entities.RendelesStatusz;
import panisz.norbert.simongumis.entities.RendelesiEgysegEntity;
import panisz.norbert.simongumis.repositories.GumikRepository;
import panisz.norbert.simongumis.repositories.RendelesRepository;
import panisz.norbert.simongumis.services.RendelesService;
import java.util.List;

@Service
@Transactional
public class RendelesServiceImpl implements RendelesService {
    @Autowired
    RendelesRepository rendelesRepository;
    @Autowired
    GumikRepository gumikRepository;

    @Override
    public List<RendelesEntity> osszes() {
        return rendelesRepository.findAll();
    }

    @Override
    public RendelesEntity ment(RendelesEntity rendelesEntity) {
        return rendelesRepository.save(rendelesEntity);
    }

    @Override
    public void torol(RendelesEntity rendelesEntity) {
        rendelesRepository.delete(rendelesEntity);
    }

    @Override
    public String mentKosarbol(RendelesEntity rendelesEntity) {
        //végső vizsgálat hogy a rendelésben szereplő gumikból a kívánt darabszám van-e raktáron
        for(RendelesiEgysegEntity rendelesiEgysegEntity : rendelesEntity.getRendelesiEgysegek()){
            GumikEntity gumikEntity = gumikRepository.findById(rendelesiEgysegEntity.getGumi().getId()).get();
            if(gumikEntity.getMennyisegRaktarban()<rendelesiEgysegEntity.getMennyiseg()){
                return (rendelesiEgysegEntity.getGumi().toString() + " típusú gumiból, maximum " + gumikEntity.getMennyisegRaktarban().toString() + " db elérhető, de a rendelésében több szerepel!");
            }
        }
        //megrendelésnél a raktárkészlatből a gumik levonása
        for(RendelesiEgysegEntity rendelesiEgysegEntity : rendelesEntity.getRendelesiEgysegek()){
            GumikEntity gumikEntity = gumikRepository.findById(rendelesiEgysegEntity.getGumi().getId()).get();
            gumikEntity.setMennyisegRaktarban(gumikEntity.getMennyisegRaktarban()-rendelesiEgysegEntity.getMennyiseg());
            try{
                gumikRepository.save(gumikEntity);
            }catch(Exception e){
                return "Mentés sikertelen";
            }
        }

        try{
            rendelesEntity.setSession("");
            rendelesRepository.save(rendelesEntity);
        }catch(Exception e){
            rendelesEntity.setSession(UI.getCurrent().getSession().getSession().getId());
            return "Mentés sikertelen";
        }

        return null;
    }

    @Override
    public void rendelesTrolese(RendelesEntity rendelesEntity){
        rendelesEntity.setStatusz(RendelesStatusz.TOROLVE);
        for(RendelesiEgysegEntity rendelesiEgysegEntity : rendelesEntity.getRendelesiEgysegek()){
            GumikEntity gumikEntity = gumikRepository.findById(rendelesiEgysegEntity.getGumi().getId()).get();
            gumikEntity.setMennyisegRaktarban(gumikEntity.getMennyisegRaktarban()+rendelesiEgysegEntity.getMennyiseg());
            gumikRepository.save(gumikEntity);
        }
        rendelesRepository.save(rendelesEntity);
    }

    @Override
    public RendelesEntity sessionreKeres(String session){
        return rendelesRepository.findBySession(session);
    }

    @Override
    public List<RendelesEntity> ugyfelNevreKeres(String nev){return rendelesRepository.findAllByUgyfel_Nev(nev);}

    @Override
    public List<RendelesEntity> rendelesekreKeres(RendelesStatusz statusz){return rendelesRepository.findAllByStatuszIsNot(statusz);}

}
