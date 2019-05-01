package panisz.norbert.simongumis.services.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import panisz.norbert.simongumis.entities.UgyfelEntity;
import panisz.norbert.simongumis.repositories.UgyfelRepository;
import panisz.norbert.simongumis.services.UgyfelService;
import java.util.List;

@Service
@Transactional
public class UgyfelServiceImpl implements UgyfelService {
    @Autowired
    UgyfelRepository ugyfelRepository;

    @Override
    public List<UgyfelEntity> osszes() {
        return ugyfelRepository.findAll();
    }

    @Override
    public UgyfelEntity ment(UgyfelEntity ugyfelEntity) {
        return ugyfelRepository.save(ugyfelEntity);
    }

    @Override
    public void torol(UgyfelEntity ugyfelEntity) {
        ugyfelRepository.delete(ugyfelEntity);
    }
}
