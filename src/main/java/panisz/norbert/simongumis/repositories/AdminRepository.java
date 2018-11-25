package panisz.norbert.simongumis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import panisz.norbert.simongumis.entities.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
}
