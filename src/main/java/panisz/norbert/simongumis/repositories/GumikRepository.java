package panisz.norbert.simongumis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import panisz.norbert.simongumis.entities.GumikEntity;

import java.util.List;

public interface GumikRepository extends JpaRepository<GumikEntity, Integer> {
    GumikEntity findByGyartoAndMeret_SzelessegAndMeret_ProfilAndMeret_FelniAndEvszakAndAllapot(String gyarto, Integer szelesseg, Integer profil, Integer felni, String evszak, String allapot);

    List<GumikEntity> findAllByMeretId(Integer id);

}
