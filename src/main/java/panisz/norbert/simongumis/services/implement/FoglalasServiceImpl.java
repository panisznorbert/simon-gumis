package panisz.norbert.simongumis.services.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import panisz.norbert.simongumis.entities.FoglalasEntity;
import panisz.norbert.simongumis.repositories.FoglalasRepository;
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
    public FoglalasEntity ment(FoglalasEntity foglalasEntity) {
        return foglalasRepository.save(foglalasEntity);
    }

    @Override
    public void torol(FoglalasEntity foglalasEntity) {
        foglalasRepository.delete(foglalasEntity);
    }
}
