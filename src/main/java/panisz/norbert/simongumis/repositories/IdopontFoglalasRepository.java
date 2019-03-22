package panisz.norbert.simongumis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import panisz.norbert.simongumis.entities.IdopontFoglalasEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface IdopontFoglalasRepository extends JpaRepository<IdopontFoglalasEntity, Integer> {

    IdopontFoglalasEntity findByDatum(LocalDateTime localDateTime);

    List<IdopontFoglalasEntity> findAllByDatumAfter(LocalDateTime localDateTime);
}
