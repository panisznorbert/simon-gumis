package panisz.norbert.simongumis.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@Table(name = "gumik")
public class GumikEntity extends BaseEntity {
    private String gyarto;
    @ManyToOne(cascade = CascadeType.ALL)
    private GumiMeretekEntity meret;
    private Integer ar;
    private String evszak;
    private String allapot;
    private Integer mennyisegRaktarban;
    @Lob
    private byte[] kep;

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

    public void beallit (MegrendeltGumikEntity megrendeltGumikEntity, int darab){
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
    }
}
