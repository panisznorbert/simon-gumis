package panisz.norbert.simongumis.services.implement;

import com.vaadin.flow.component.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import panisz.norbert.simongumis.entities.*;
import panisz.norbert.simongumis.repositories.GumiMeretekRepository;
import panisz.norbert.simongumis.repositories.GumikRepository;
import panisz.norbert.simongumis.repositories.MegrendeltGumikRepository;
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
    @Autowired
    MegrendeltGumikRepository megrendeltGumikRepository;
    @Autowired
    GumiMeretekRepository gumiMeretekRepository;

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
            GumikEntity gumikEntity = gumikRepository.findById(rendelesiEgysegEntity.getGumi().getGumiId()).get();
            if(gumikEntity.getMennyisegRaktarban()<rendelesiEgysegEntity.getMennyiseg()){
                return (rendelesiEgysegEntity.getGumi().toString() + " típusú gumiból, maximum " + gumikEntity.getMennyisegRaktarban().toString() + " db elérhető, de a rendelésében több szerepel!");
            }
        }

        //megrendelésnél a raktárkészlatből a gumik levonása
        for(RendelesiEgysegEntity rendelesiEgysegEntity : rendelesEntity.getRendelesiEgysegek()){
            GumikEntity gumikEntity = gumikRepository.findById(rendelesiEgysegEntity.getGumi().getGumiId()).get();
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

            GumikEntity gumikEntity = gumikRepository.findByGyartoAndMeret_SzelessegAndMeret_ProfilAndMeret_FelniAndEvszakAndAllapot(
                    rendelesiEgysegEntity.getGumi().getGyarto(),
                    rendelesiEgysegEntity.getGumi().getMeretSzelesseg(),
                    rendelesiEgysegEntity.getGumi().getMeretProfil(),
                    rendelesiEgysegEntity.getGumi().getMeretFelni(),
                    rendelesiEgysegEntity.getGumi().getEvszak(),
                    rendelesiEgysegEntity.getGumi().getAllapot()
            );
            //Amennyiben van ilyen gumi az adatbázisban visszateszi hozzá a darabszámot
            if(gumikEntity!=null){
                gumikEntity.setMennyisegRaktarban(gumikEntity.getMennyisegRaktarban()+rendelesiEgysegEntity.getMennyiseg());
                gumikRepository.save(gumikEntity);
            //Amennyiben nincs ilyen gumi az adatbázisban létrehozza
            }else{
                gumikEntity = new GumikEntity();
                gumikEntity.beallit(rendelesiEgysegEntity.getGumi(), rendelesiEgysegEntity.getMennyiseg());

                //vizsgálni hogy van-e már ilyen méret lementve és ha igen ne mentsunk még egyet le
                GumiMeretekEntity meret = gumiMeretekRepository.findBySzelessegAndProfilAndFelni(
                        gumikEntity.getMeret().getSzelesseg(),
                        gumikEntity.getMeret().getProfil(),
                        gumikEntity.getMeret().getFelni());
                if(meret != null){
                    gumikEntity.setMeret(meret);
                }

                gumikRepository.save(gumikEntity);

            }

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
