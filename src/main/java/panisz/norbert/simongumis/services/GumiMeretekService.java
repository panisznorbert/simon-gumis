package panisz.norbert.simongumis.services;

import panisz.norbert.simongumis.entities.GumiMeretekEntity;
import java.util.List;

public interface GumiMeretekService extends BaseServices<GumiMeretekEntity> {

    List<GumiMeretekEntity> szelessegreKeres(Integer szelesseg);
    List<GumiMeretekEntity> profilraKeres(Integer profil);
    List<GumiMeretekEntity> felnireKeres(Integer felni);

    List<GumiMeretekEntity> szelessegreEsProfilraKeres(Integer szelesseg, Integer profil);
    List<GumiMeretekEntity> szelessegreEsFelnireKeres(Integer szelesseg, Integer felni);
    List<GumiMeretekEntity> profilraEsFelnireKeres(Integer profil, Integer felni);

    GumiMeretekEntity mindenMeretreKeres(Integer szelesseg, Integer profil, Integer felni);
}
