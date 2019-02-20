package panisz.norbert.simongumis.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class RendelesEntity extends BaseEntity {
    @OneToOne(cascade = CascadeType.ALL)
    private UgyfelEntity ugyfel;

    @OneToMany(cascade = CascadeType.ALL,  fetch=FetchType.EAGER)
    private List<RendelesiEgysegEntity> rendelesiEgysegek;

    private Integer vegosszeg;

    @Enumerated(EnumType.STRING)
    private RendelesStatusz statusz;
}
