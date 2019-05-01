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
@Table(name = "idopontfoglalasok")
public class IdopontfoglalasEntity extends BaseEntity implements Comparable<IdopontfoglalasEntity>{
    private LocalDateTime datum;
    @ManyToOne(cascade = CascadeType.ALL)
    private UgyfelEntity ugyfel;
    private String megjegyzes;


    @Override
    public int compareTo(IdopontfoglalasEntity o) {
        return this.getDatum().compareTo((o.getDatum()));
    }
}
