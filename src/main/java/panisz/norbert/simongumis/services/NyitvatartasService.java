package panisz.norbert.simongumis.services;

import panisz.norbert.simongumis.entities.NyitvatartasEntity;

import java.time.LocalDate;

public interface NyitvatartasService  extends BaseServices<NyitvatartasEntity>  {

    NyitvatartasEntity adottNapNyitvatartasa(LocalDate localDate);
}
