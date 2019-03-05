package panisz.norbert.simongumis.services.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import panisz.norbert.simongumis.entities.RendelesEntity;
import panisz.norbert.simongumis.repositories.RendelesRepository;
import panisz.norbert.simongumis.services.RendelesService;
import java.util.List;

@Service
@Transactional
public class RendelesServiceImpl implements RendelesService {
    @Autowired
    RendelesRepository rendelesRepository;

    @Override
    public List<RendelesEntity> osszes() {
        return rendelesRepository.findAll();
    }

    @Override
    public RendelesEntity ment(RendelesEntity rendelesEntity) {
        return rendelesRepository.save(rendelesEntity);
    }

    @Override
    public void torol(RendelesEntity rendelesEntity) {
        rendelesRepository.delete(rendelesEntity);
    }

    public RendelesEntity idKereses(Integer id){
        return rendelesRepository.findById(id).get();
    }

}
