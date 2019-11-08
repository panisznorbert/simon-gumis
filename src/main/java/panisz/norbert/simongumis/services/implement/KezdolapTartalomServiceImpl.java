package panisz.norbert.simongumis.services.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import panisz.norbert.simongumis.entities.KezdolapTartalmiElemek;
import panisz.norbert.simongumis.entities.KezdolapTartalomEntity;
import panisz.norbert.simongumis.repositories.KezdolapTartalomRepository;
import panisz.norbert.simongumis.services.KezdolapTartalomService;

import java.util.List;

@Service
@Transactional
public class KezdolapTartalomServiceImpl implements KezdolapTartalomService {
    @Autowired
    KezdolapTartalomRepository kezdolapTartalomRepository;
    @Override
    public List<KezdolapTartalomEntity> osszes() {
        return kezdolapTartalomRepository.findAll();
    }

    @Override
    public KezdolapTartalomEntity ment(KezdolapTartalomEntity kezdolapTartalomEntity){
        return kezdolapTartalomRepository.save(kezdolapTartalomEntity);
    }

    @Override
    public void torol(KezdolapTartalomEntity kezdolapTartalomEntity) {
        kezdolapTartalomRepository.delete(kezdolapTartalomEntity);
    }

    @Override
    public KezdolapTartalomEntity aktualisSzorolap() {
        if(kezdolapTartalomRepository.findByMegnevezes(KezdolapTartalmiElemek.SZOROLAP) != null){
            return kezdolapTartalomRepository.findByMegnevezes(KezdolapTartalmiElemek.SZOROLAP);
        }else{
            return null;
        }
    }

    @Override
    public List<KezdolapTartalomEntity> egyebTartalom() {
        return kezdolapTartalomRepository.findAllByMegnevezes(KezdolapTartalmiElemek.TARTALOM);
    }
}
