package panisz.norbert.simongumis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import panisz.norbert.simongumis.entities.GumikEntity;

public interface GumikRepository extends JpaRepository<GumikEntity, Integer> {
}
