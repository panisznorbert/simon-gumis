package panisz.norbert.simongumis.services;

import panisz.norbert.simongumis.entities.GumikEntity;

import java.util.List;

public interface GumikService extends BaseServices<GumikEntity> {
    GumikEntity vanMarIlyen(String gyarto, Integer szelesseg, Integer profil, Integer felni, String evszak, String allapot);
    List<GumikEntity> keresesMeretId(Integer id);
}
