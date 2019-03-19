package panisz.norbert.simongumis.services;

import panisz.norbert.simongumis.entities.FoglalasEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface FoglalasService extends BaseServices<FoglalasEntity> {

    FoglalasEntity keresesDatumra(LocalDateTime localDateTime);

    List<FoglalasEntity> keresesNaptol(LocalDate localDate);
}
