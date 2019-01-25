package panisz.norbert.simongumis.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class RendelesiEgysegEntity extends BaseEntity {
    @OneToOne
    private GumikEntity gumi;
    private Integer mennyiseg;
    private Integer reszosszeg;
}
