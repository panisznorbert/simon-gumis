package panisz.norbert.simongumis.services.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import panisz.norbert.simongumis.entities.IdopontfoglalasEntity;
import panisz.norbert.simongumis.entities.UgyfelEntity;
import panisz.norbert.simongumis.repositories.IdopontfoglalasRepository;
import panisz.norbert.simongumis.repositories.UgyfelRepository;
import panisz.norbert.simongumis.services.IdopontfoglalasServie;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
public class IdopontfoglalasServieImpl implements IdopontfoglalasServie {
    @Autowired
    IdopontfoglalasRepository idopontfoglalasRepository;
    @Autowired
    UgyfelRepository ugyfelRepository;

    @Override
    public IdopontfoglalasEntity keresesDatumra(LocalDateTime localDateTime) {
        return idopontfoglalasRepository.findByDatum(localDateTime);
    }

    @Override
    public List<IdopontfoglalasEntity> keresesNaptol(LocalDateTime localDateTime) {
        return idopontfoglalasRepository.findAllByDatumAfter(localDateTime);
    }

    @Override
    public List<IdopontfoglalasEntity> keresesMa() {
        List<IdopontfoglalasEntity> foglalasok = idopontfoglalasRepository.findAllByDatumAfter(LocalDateTime.of(LocalDate.now(), LocalTime.now()));
        foglalasok.removeAll(idopontfoglalasRepository.findAllByDatumAfter(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(0, 0))));
        return foglalasok;
    }

    @Override
    public List<IdopontfoglalasEntity> osszes() {
        return idopontfoglalasRepository.findAll();
    }

    @Override
    public IdopontfoglalasEntity ment(IdopontfoglalasEntity idopontFoglalasEntity) {
        //Megvizsgálni, hogy van-e már ilyen ügyfél és ha van akkor azt használni
        UgyfelEntity ugyfelEntity = ugyfelRepository.findByNevAndTelefonAndEmail(
                idopontFoglalasEntity.getUgyfel().getNev(),
                idopontFoglalasEntity.getUgyfel().getTelefon(),
                idopontFoglalasEntity.getUgyfel().getEmail());
        if(ugyfelEntity != null) {
            idopontFoglalasEntity.setUgyfel(ugyfelEntity);
        }
        return idopontfoglalasRepository.save(idopontFoglalasEntity);
    }

    @Override
    public void torol(IdopontfoglalasEntity idopontFoglalasEntity) {
        idopontFoglalasEntity.setUgyfel(null);
        idopontfoglalasRepository.delete(idopontFoglalasEntity);
    }
}
