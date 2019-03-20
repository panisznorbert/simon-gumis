package panisz.norbert.simongumis.services.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import panisz.norbert.simongumis.entities.FoglalasEntity;
import panisz.norbert.simongumis.entities.UgyfelEntity;
import panisz.norbert.simongumis.repositories.FoglalasRepository;
import panisz.norbert.simongumis.repositories.UgyfelRepository;
import panisz.norbert.simongumis.services.FoglalasService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
public class FoglalasServiceImpl implements FoglalasService {
    @Autowired
    FoglalasRepository foglalasRepository;
    @Autowired
    UgyfelRepository ugyfelRepository;

    @Override
    public FoglalasEntity keresesDatumra(LocalDateTime localDateTime) {
        return foglalasRepository.findByDatum(localDateTime);
    }

    @Override
    public List<FoglalasEntity> keresesNaptol(LocalDate localDate) {
        return foglalasRepository.findAllByDatumAfter(LocalDateTime.of(localDate, LocalTime.of(0, 0)));
    }

    @Override
    public List<FoglalasEntity> osszes() {
        return foglalasRepository.findAll();
    }

    @Override
    public String ment(FoglalasEntity foglalasEntity) {
        String hiba = null;
        //Megvizsgálni, hogy van-e már ilyen ügyfél és ha van akkor azt használni
        UgyfelEntity ugyfelEntity = ugyfelRepository.findByNevAndTelefonAndEmail(
                foglalasEntity.getUgyfel().getNev(),
                foglalasEntity.getUgyfel().getTelefon(),
                foglalasEntity.getUgyfel().getEmail());
        if(ugyfelEntity != null) {
            foglalasEntity.setUgyfel(ugyfelEntity);
        }
        try{
            foglalasRepository.save(foglalasEntity);
        }catch(Exception e){
            hiba = "Mentés sikertelen";
        }
        return hiba;
    }

    @Override
    public void torol(FoglalasEntity foglalasEntity) {
        foglalasEntity.setUgyfel(null);
        foglalasRepository.delete(foglalasEntity);
    }
}
