package panisz.norbert.simongumis.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class RendelesEntity extends BaseEntity {
    @OneToOne
    private UgyfelEntity ugyfel;
    @OneToMany
    private List<RendelesiEgysegEntity> rendelesiEgysegek;
    private Integer vegosszeg;
    private String statusz;
}
