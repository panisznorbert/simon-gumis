package panisz.norbert.simongumis.services;

import panisz.norbert.simongumis.entities.GumiMeretekEntity;
import java.util.List;

public interface GumiMeretekService extends BaseServices<GumiMeretekEntity> {

    List<GumiMeretekEntity> szelessegreKeresMenuhoz(Integer szelesseg);
    List<GumiMeretekEntity> profilraKeresMenuhoz(Integer profil);
    List<GumiMeretekEntity> felnireKeresMenuhoz(Integer felni);

    List<GumiMeretekEntity> szelessegreEsProfilraKeresMenuhoz(Integer szelesseg, Integer profil);
    List<GumiMeretekEntity> szelessegreEsFelnireKeresMenuhoz(Integer szelesseg, Integer felni);
    List<GumiMeretekEntity> profilraEsFelnireKeresMenuhoz(Integer profil, Integer felni);

    List<GumiMeretekEntity> osszesMenuhoz();

    List<GumiMeretekEntity> amihezVanGumi(List<GumiMeretekEntity> gumiMeretek);
}
