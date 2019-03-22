package panisz.norbert.simongumis.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "rendelesi_egysegek")
public class RendelesiEgysegEntity extends BaseEntity {
    @OneToOne
    private GumikEntity gumi;
    private Integer mennyiseg;
    private Integer reszosszeg;

    @Override
    public String toString() {
        return gumi.toString() + ", " + mennyiseg + " db," + reszosszeg + " Ft";
    }
}
