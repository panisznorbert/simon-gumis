package panisz.norbert.simongumis.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "megrendelt_gumik")
public class MegrendeltGumikEntity extends BaseEntity {

    private Integer gumiId;
    private String gyarto;
    private Integer meretSzelesseg;
    private Integer meretProfil;
    private Integer meretFelni;
    private Integer ar;
    private String evszak;
    private String allapot;
    @Lob
    private byte[] kep;

    @Override
    public String toString() {
        return gyarto + " " + meretSzelesseg + "/" + meretProfil + "R" + meretFelni + " " + evszak + "-" + allapot;
    }

    public MegrendeltGumikEntity beallit(GumikEntity gumi) {
        this.setGumiId(gumi.getId());
        this.setAllapot(gumi.getAllapot());
        this.setAr(gumi.getAr());
        this.setEvszak(gumi.getEvszak());
        this.setGyarto(gumi.getGyarto());
        this.setMeretFelni(gumi.getMeret().getFelni());
        this.setMeretProfil(gumi.getMeret().getProfil());
        this.setMeretSzelesseg(gumi.getMeret().getSzelesseg());
        this.setKep(gumi.getKep());

        return this;
    }
}
