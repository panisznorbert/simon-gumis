package panisz.norbert.simongumis.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class FoglalasEntity extends BaseEntity {
    private Date datum;
    private int ugyfel_id;
    private String statusz;
    private String megjegyzes;

}
