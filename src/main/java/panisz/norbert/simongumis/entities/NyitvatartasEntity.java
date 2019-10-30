package panisz.norbert.simongumis.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "nyitvatartas")
public class NyitvatartasEntity  extends BaseEntity implements Comparable<NyitvatartasEntity>{
    private LocalDate datum;
    private LocalTime nyitas;
    private LocalTime zaras;

    @Override
    public int compareTo(NyitvatartasEntity nyitvatartasEntity) {
        return this.getDatum().compareTo((nyitvatartasEntity.getDatum()));
    }

    @Override
    public String toString() {
        return datum.toString() + ": " + nyitas + " - " + zaras;
    }
}
