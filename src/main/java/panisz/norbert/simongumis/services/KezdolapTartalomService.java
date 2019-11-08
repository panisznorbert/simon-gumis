package panisz.norbert.simongumis.services;

import panisz.norbert.simongumis.entities.KezdolapTartalomEntity;

import java.util.List;

public interface KezdolapTartalomService extends BaseServices<KezdolapTartalomEntity> {
    KezdolapTartalomEntity aktualisSzorolap();
    List<KezdolapTartalomEntity> egyebTartalom();
}
