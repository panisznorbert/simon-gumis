package panisz.norbert.simongumis.repositories;

        import org.springframework.data.jpa.repository.JpaRepository;
        import panisz.norbert.simongumis.entities.IdopontfoglalasEntity;
        import java.time.LocalDateTime;
        import java.util.List;

public interface IdopontfoglalasRepository extends JpaRepository<IdopontfoglalasEntity, Integer> {

    IdopontfoglalasEntity findByDatum(LocalDateTime localDateTime);

    List<IdopontfoglalasEntity> findAllByDatumAfter(LocalDateTime localDateTime);
}