package panisz.norbert.simongumis.services.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import panisz.norbert.simongumis.entities.IdopontFoglalasEntity;
import panisz.norbert.simongumis.entities.UgyfelEntity;
import panisz.norbert.simongumis.repositories.IdopontFoglalasRepository;
import panisz.norbert.simongumis.repositories.UgyfelRepository;
import panisz.norbert.simongumis.services.IdopontFoglalasService;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class IdopontFoglalasServiceImpl implements IdopontFoglalasService {
    @Autowired
    IdopontFoglalasRepository idopontFoglalasRepository;
    @Autowired
    UgyfelRepository ugyfelRepository;

    @Override
    public IdopontFoglalasEntity keresesDatumra(LocalDateTime localDateTime) {
        return idopontFoglalasRepository.findByDatum(localDateTime);
    }

    @Override
    public List<IdopontFoglalasEntity> keresesNaptol(LocalDateTime localDateTime) {
        return idopontFoglalasRepository.findAllByDatumAfter(localDateTime);
    }

    @Override
    public List<IdopontFoglalasEntity> osszes() {
        return idopontFoglalasRepository.findAll();
    }

    @Override
    public IdopontFoglalasEntity ment(IdopontFoglalasEntity idopontFoglalasEntity) {
        //Megvizsgálni, hogy van-e már ilyen ügyfél és ha van akkor azt használni
        UgyfelEntity ugyfelEntity = ugyfelRepository.findByNevAndTelefonAndEmail(
                idopontFoglalasEntity.getUgyfel().getNev(),
                idopontFoglalasEntity.getUgyfel().getTelefon(),
                idopontFoglalasEntity.getUgyfel().getEmail());
        if(ugyfelEntity != null) {
            idopontFoglalasEntity.setUgyfel(ugyfelEntity);
        }
        return idopontFoglalasRepository.save(idopontFoglalasEntity);
    }

    @Override
    public void torol(IdopontFoglalasEntity idopontFoglalasEntity) {
        idopontFoglalasEntity.setUgyfel(null);
        idopontFoglalasRepository.delete(idopontFoglalasEntity);
    }
}
