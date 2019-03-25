package panisz.norbert.simongumis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import panisz.norbert.simongumis.entities.RendelesEntity;

import java.util.List;

public interface RendelesRepository extends JpaRepository<RendelesEntity, Integer> {
    List<RendelesEntity> findAllByUgyfel_Nev (String nev);
}
