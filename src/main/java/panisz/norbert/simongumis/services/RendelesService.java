package panisz.norbert.simongumis.services;

import panisz.norbert.simongumis.entities.RendelesEntity;
import panisz.norbert.simongumis.entities.RendelesStatusz;
import panisz.norbert.simongumis.entities.RendelesiEgysegEntity;

import java.util.List;

public interface RendelesService extends BaseServices<RendelesEntity> {

    String mentKosarbol(RendelesEntity rendelesEntity);

    void rendelesTrolese(RendelesEntity rendelesEntity);

    RendelesEntity sessionreKeres(String session);

    List<RendelesEntity> ugyfelNevreKeres(String nev);

    List<RendelesEntity> rendelesekreKeres(RendelesStatusz statusz);

    Integer gumiDarabszamRaktaron(RendelesiEgysegEntity rendelesiEgysegEntity);

}
