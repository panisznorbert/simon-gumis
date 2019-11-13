package panisz.norbert.simongumis.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "kezdolap_tartalmak")
public class KezdolapTartalomEntity extends BaseEntity implements Comparable<KezdolapTartalomEntity>{
    @Enumerated(EnumType.STRING)
    private KezdolapTartalmiElemek megnevezes;
    @Lob
    private byte[] kep;
    private String kepMeret;
    private String leiras;
    private Integer pozicio;

    @Override
    public int compareTo(KezdolapTartalomEntity kezdolapTartalomEntity) {
        return this.pozicio.compareTo(kezdolapTartalomEntity.pozicio);
    }
}
