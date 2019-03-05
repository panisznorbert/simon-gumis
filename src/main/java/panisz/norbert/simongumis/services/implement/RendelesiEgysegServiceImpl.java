package panisz.norbert.simongumis.services.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import panisz.norbert.simongumis.entities.RendelesiEgysegEntity;
import panisz.norbert.simongumis.repositories.RendelesiEgysegRepository;
import panisz.norbert.simongumis.services.RendelesiEgysegService;
import java.util.List;

@Service
@Transactional
public class RendelesiEgysegServiceImpl implements RendelesiEgysegService {
    @Autowired
    RendelesiEgysegRepository rendelesiEgysegRepository;

    @Override
    public List<RendelesiEgysegEntity> osszes() {
        return rendelesiEgysegRepository.findAll();
    }

    @Override
    public RendelesiEgysegEntity ment(RendelesiEgysegEntity rendelesiEgysegEntity) {
        return rendelesiEgysegRepository.save(rendelesiEgysegEntity);
    }

    @Override
    public void torol(RendelesiEgysegEntity rendelesiEgysegEntity) {
        rendelesiEgysegRepository.delete(rendelesiEgysegEntity);
    }
}
