package panisz.norbert.simongumis.services;

import panisz.norbert.simongumis.entities.IdopontFoglalasEntity;
import java.time.LocalDateTime;
import java.util.List;

public interface IdopontFoglalasService extends BaseServices<IdopontFoglalasEntity> {

    IdopontFoglalasEntity keresesDatumra(LocalDateTime localDateTime);

    List<IdopontFoglalasEntity> keresesNaptol(LocalDateTime localDateTime);
}
