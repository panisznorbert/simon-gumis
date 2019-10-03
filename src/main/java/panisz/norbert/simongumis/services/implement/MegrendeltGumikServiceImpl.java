package panisz.norbert.simongumis.services.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import panisz.norbert.simongumis.entities.MegrendeltGumikEntity;
import panisz.norbert.simongumis.repositories.MegrendeltGumikRepository;
import panisz.norbert.simongumis.services.MegrendeltGumikService;
import java.util.List;

@Service
@Transactional
public class MegrendeltGumikServiceImpl implements MegrendeltGumikService {
    @Autowired
    MegrendeltGumikRepository megrendeltGumikRepository;

    @Override
    public List<MegrendeltGumikEntity> osszes() {
        return megrendeltGumikRepository.findAll();
    }

    @Override
    public MegrendeltGumikEntity ment(MegrendeltGumikEntity megrendeltGumikEntity){
            return megrendeltGumikRepository.save(megrendeltGumikEntity);
    }

    @Override
    public void torol(MegrendeltGumikEntity megrendeltGumikEntity) {
        megrendeltGumikRepository.delete(megrendeltGumikEntity);
    }

}
