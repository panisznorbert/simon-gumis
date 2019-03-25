package panisz.norbert.simongumis.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "rendelesek")
public class RendelesEntity extends BaseEntity implements Comparable<RendelesEntity>{
    @OneToOne(cascade = CascadeType.ALL)
    private UgyfelEntity ugyfel;

    @OneToMany(cascade = CascadeType.ALL,  fetch=FetchType.EAGER)
    private List<RendelesiEgysegEntity> rendelesiEgysegek;

    private Integer vegosszeg;

    @Enumerated(EnumType.STRING)
    private RendelesStatusz statusz;

    private LocalDate datum;

    @Override
    public int compareTo(RendelesEntity o) {
        return this.getDatum().compareTo(o.getDatum());
    }
}
