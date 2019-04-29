package panisz.norbert.simongumis.services.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import panisz.norbert.simongumis.entities.AdminEntity;
import panisz.norbert.simongumis.repositories.AdminRepository;
import panisz.norbert.simongumis.services.AdminService;

import java.util.List;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminRepository adminRepository;

    @Override
    public AdminEntity sessionreKeres(String session) {
        return adminRepository.findBySession(session);
    }

    @Override
    public AdminEntity adminraKeres(String nev, String jelszo) {
        return adminRepository.findByNevAndJelszo(nev, jelszo);
    }

    @Override
    public List<AdminEntity> osszes() {
        return adminRepository.findAll();
    }

    @Override
    public AdminEntity ment(AdminEntity adminEntity){
        return adminRepository.save(adminEntity);
    }

    @Override
    public void torol(AdminEntity adminEntity) {
        adminRepository.delete(adminEntity);
    }
}
