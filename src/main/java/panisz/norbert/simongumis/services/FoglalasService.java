package panisz.norbert.simongumis.services;

import panisz.norbert.simongumis.entities.FoglalasEntity;
import java.time.LocalDateTime;

public interface FoglalasService extends BaseServices<FoglalasEntity> {

    FoglalasEntity keresesDatumra(LocalDateTime localDateTime);
}
