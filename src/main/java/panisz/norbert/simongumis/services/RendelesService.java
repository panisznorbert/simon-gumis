package panisz.norbert.simongumis.services;

import panisz.norbert.simongumis.entities.RendelesEntity;
import panisz.norbert.simongumis.entities.RendelesStatusz;

import java.util.List;

public interface RendelesService extends BaseServices<RendelesEntity> {

    String mentKosarbol(RendelesEntity rendelesEntity);

    void rendelesTrolese(RendelesEntity rendelesEntity);

    RendelesEntity idKereses(Integer id);

    RendelesEntity tokenreKeres(String token);

    List<RendelesEntity> ugyfelNevreKeres(String nev);

    List<RendelesEntity> rendelesekreKeres(RendelesStatusz statusz);
}
