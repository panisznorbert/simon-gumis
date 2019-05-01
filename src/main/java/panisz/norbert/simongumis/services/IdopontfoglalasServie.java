package panisz.norbert.simongumis.services;

import panisz.norbert.simongumis.entities.IdopontfoglalasEntity;
import java.time.LocalDateTime;
import java.util.List;

public interface IdopontfoglalasServie extends BaseServices<IdopontfoglalasEntity> {

    IdopontfoglalasEntity keresesDatumra(LocalDateTime localDateTime);

    List<IdopontfoglalasEntity> keresesNaptol(LocalDateTime localDateTime);
}
