package panisz.norbert.simongumis.services;

import panisz.norbert.simongumis.entities.RendelesEntity;

import java.util.List;

public interface RendelesService extends BaseServices<RendelesEntity> {

    String mentKosarbol(RendelesEntity rendelesEntity);

    void rendelesTrolese(RendelesEntity rendelesEntity);

    RendelesEntity idKereses(Integer id);

    List<RendelesEntity> ugyfelNevreKeres(String nev);
}
