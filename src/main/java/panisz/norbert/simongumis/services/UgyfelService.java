package panisz.norbert.simongumis.services;

import panisz.norbert.simongumis.entities.UgyfelEntity;

public interface UgyfelService extends BaseServices<UgyfelEntity> {
    UgyfelEntity keresesUgyfeladatokra(String nev, String telefon, String email);
}
