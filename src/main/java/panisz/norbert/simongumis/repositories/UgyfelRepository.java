package panisz.norbert.simongumis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import panisz.norbert.simongumis.entities.UgyfelEntity;

@Repository
public interface UgyfelRepository extends JpaRepository<UgyfelEntity, Integer> {

    UgyfelEntity findByNevAndTelefonAndEmail(String nev, String telefon, String email);
}
