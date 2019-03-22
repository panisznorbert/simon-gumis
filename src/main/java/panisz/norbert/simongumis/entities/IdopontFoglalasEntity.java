package panisz.norbert.simongumis.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "idopont_foglalasok")
public class IdopontFoglalasEntity extends BaseEntity implements Comparable<IdopontFoglalasEntity>{
    private LocalDateTime datum;
    @ManyToOne(cascade = CascadeType.ALL)
    private UgyfelEntity ugyfel;
    private String megjegyzes;


    @Override
    public int compareTo(IdopontFoglalasEntity o) {
        return this.getDatum().compareTo((o.getDatum()));
    }
}
