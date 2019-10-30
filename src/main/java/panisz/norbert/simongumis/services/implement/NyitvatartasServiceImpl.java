package panisz.norbert.simongumis.services.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import panisz.norbert.simongumis.entities.NyitvatartasEntity;
import panisz.norbert.simongumis.repositories.NyitvatartasRepository;
import panisz.norbert.simongumis.services.NyitvatartasService;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class NyitvatartasServiceImpl implements NyitvatartasService {
    @Autowired
    NyitvatartasRepository nyitvatartasRepository;

    @Override
    public List<NyitvatartasEntity> osszes() {
        return nyitvatartasRepository.findAll();
    }

    @Override
    public NyitvatartasEntity ment(NyitvatartasEntity nyitvatartasEntity) throws Exception {
        return nyitvatartasRepository.save(nyitvatartasEntity);
    }

    @Override
    public void torol(NyitvatartasEntity nyitvatartasEntity) {
        nyitvatartasRepository.delete(nyitvatartasEntity);
    }

    @Override
    public NyitvatartasEntity adottNapNyitvatartasa(LocalDate localDate) {
        return nyitvatartasRepository.findByDatum(localDate);
    }
}
