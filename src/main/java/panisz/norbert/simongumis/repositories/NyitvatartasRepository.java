package panisz.norbert.simongumis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import panisz.norbert.simongumis.entities.NyitvatartasEntity;

import java.time.LocalDate;

public interface NyitvatartasRepository extends JpaRepository<NyitvatartasEntity, Integer> {

    NyitvatartasEntity findByDatum(LocalDate localDate);
}
