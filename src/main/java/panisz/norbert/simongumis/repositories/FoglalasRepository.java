package panisz.norbert.simongumis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import panisz.norbert.simongumis.entities.FoglalasEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface FoglalasRepository extends JpaRepository<FoglalasEntity, Integer> {

    FoglalasEntity findByDatum(LocalDateTime localDateTime);

    List<FoglalasEntity> findAllByDatumIs(LocalDate localDate);
}
