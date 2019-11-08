package panisz.norbert.simongumis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import panisz.norbert.simongumis.entities.KezdolapTartalmiElemek;
import panisz.norbert.simongumis.entities.KezdolapTartalomEntity;

import java.util.List;

public interface KezdolapTartalomRepository extends JpaRepository<KezdolapTartalomEntity, Integer> {
    List<KezdolapTartalomEntity> findAllByMegnevezes(KezdolapTartalmiElemek megnevezes);

    KezdolapTartalomEntity findByMegnevezes(KezdolapTartalmiElemek megnevezes);
}
