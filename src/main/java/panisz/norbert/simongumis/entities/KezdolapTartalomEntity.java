package panisz.norbert.simongumis.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;
import java.time.LocalDateTime;

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
    private LocalDateTime pozicio;

    @Override
    public int compareTo(KezdolapTartalomEntity kezdolapTartalomEntity) {
        return kezdolapTartalomEntity.pozicio.compareTo(this.pozicio);
    }
}
