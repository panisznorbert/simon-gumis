package panisz.norbert.simongumis.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import panisz.norbert.simongumis.entities.Admin;
import panisz.norbert.simongumis.repositories.AdminRepository;

import java.util.List;
@RequestMapping(path = "/admins")
@RestController
public class AdminController {
    @Autowired
    private AdminRepository adminRepository;
    @GetMapping(path="/")
    public ResponseEntity<List<Admin>> getAdmins(){
        return ResponseEntity.ok(adminRepository.findAll());
    }

}
