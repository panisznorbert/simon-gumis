package panisz.norbert.simongumis.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class FoglalasEntity extends BaseEntity {
    private Date datum;
    @ManyToOne
    private UgyfelEntity ugyfel;
    private String megjegyzes;

}
