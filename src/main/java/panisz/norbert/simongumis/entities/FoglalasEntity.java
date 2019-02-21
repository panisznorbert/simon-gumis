package panisz.norbert.simongumis.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class FoglalasEntity extends BaseEntity {
    private LocalDateTime datum;
    @ManyToOne(cascade = CascadeType.ALL)
    private UgyfelEntity ugyfel;
    private String megjegyzes;

}
