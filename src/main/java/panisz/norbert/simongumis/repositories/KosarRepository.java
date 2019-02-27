package panisz.norbert.simongumis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import panisz.norbert.simongumis.entities.RendelesEntity;

public interface KosarRepository extends JpaRepository<RendelesEntity, Integer> {
}
