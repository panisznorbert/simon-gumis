package panisz.norbert.simongumis.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "gumik")
public class GumikEntity extends BaseEntity {
    private String gyarto;
    @ManyToOne(cascade = CascadeType.MERGE)
    private GumiMeretekEntity meret;
    private Integer ar;
    private String evszak;
    private String allapot;
    private Integer mennyisegRaktarban;

    @Override
    public String toString() {
        return gyarto + " " + meret + " " + evszak + " " + allapot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GumikEntity that = (GumikEntity) o;
        return gyarto.equalsIgnoreCase(that.gyarto) &&
                meret.equals(that.meret) &&
                evszak.equals(that.evszak) &&
                allapot.equals(that.allapot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gyarto, meret, evszak, allapot);
    }

    public GumikEntity beallit (MegrendeltGumikEntity megrendeltGumikEntity, int darab){
        this.setAllapot(megrendeltGumikEntity.getAllapot());
        this.setEvszak(megrendeltGumikEntity.getEvszak());
        this.setGyarto(megrendeltGumikEntity.getGyarto());
        this.setMennyisegRaktarban(darab);
        this.setAr(megrendeltGumikEntity.getAr());
        GumiMeretekEntity gumiMeretekEntity = new GumiMeretekEntity();
        gumiMeretekEntity.setFelni(megrendeltGumikEntity.getMeretFelni());
        gumiMeretekEntity.setProfil(megrendeltGumikEntity.getMeretProfil());
        gumiMeretekEntity.setSzelesseg(megrendeltGumikEntity.getMeretSzelesseg());
        this.setMeret(gumiMeretekEntity);

        return this;
    }
}
