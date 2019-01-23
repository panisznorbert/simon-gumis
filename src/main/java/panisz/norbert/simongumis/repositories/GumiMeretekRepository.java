package panisz.norbert.simongumis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import panisz.norbert.simongumis.entities.GumiMeretekEntity;

import java.util.List;

public interface GumiMeretekRepository extends JpaRepository<GumiMeretekEntity, Integer> {

    List<GumiMeretekEntity> findAllBySzelesseg(Integer szelesseg);
    List<GumiMeretekEntity> findAllByProfil(Integer profil);
    List<GumiMeretekEntity> findAllByFelni(Integer felni);

    List<GumiMeretekEntity> findAllBySzelessegAndProfil(Integer szelesseg, Integer profil);
    List<GumiMeretekEntity> findAllBySzelessegAndFelni(Integer szelesseg, Integer felni);
    List<GumiMeretekEntity> findAllByProfilAndFelni(Integer profil, Integer felni);

}
