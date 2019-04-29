package panisz.norbert.simongumis.services;

import panisz.norbert.simongumis.entities.AdminEntity;

public interface AdminService extends BaseServices<AdminEntity> {
    AdminEntity sessionreKeres(String session);
    AdminEntity adminraKeres(String nev, String jelszo);
}
