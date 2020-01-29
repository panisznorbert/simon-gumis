package panisz.norbert.simongumis.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "rendelesi_egysegek")
public class RendelesiEgysegEntity extends BaseEntity implements Comparable<RendelesiEgysegEntity>{
    @OneToOne(cascade = CascadeType.ALL)
    private MegrendeltGumikEntity gumi;
    private Integer mennyiseg;
    private Integer reszosszeg;

    @Override
    public String toString() {
        return gumi.toString() + ", " + mennyiseg + " db," + reszosszeg + " Ft";
    }

    @Override
    public int compareTo(RendelesiEgysegEntity rendelesiEgysegEntity) {
        if(this.getGumi().getGyarto().compareTo(rendelesiEgysegEntity.getGumi().getGyarto()) != 0){
            return this.getGumi().getGyarto().compareTo(rendelesiEgysegEntity.getGumi().getGyarto());
        }
        GumiMeretekEntity gumi1 = new GumiMeretekEntity();
        gumi1.setSzelesseg(this.getGumi().getMeretSzelesseg());
        gumi1.setProfil(this.getGumi().getMeretProfil());
        gumi1.setFelni(this.getGumi().getMeretFelni());

        GumiMeretekEntity gumi2 = new GumiMeretekEntity();
        gumi2.setSzelesseg(rendelesiEgysegEntity.getGumi().getMeretSzelesseg());
        gumi2.setProfil(rendelesiEgysegEntity.getGumi().getMeretProfil());
        gumi2.setFelni(rendelesiEgysegEntity.getGumi().getMeretFelni());
        return gumi1.compareTo(gumi2);
    }
}
