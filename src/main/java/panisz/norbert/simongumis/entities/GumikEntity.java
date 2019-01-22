package panisz.norbert.simongumis.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class GumikEntity extends BaseEntity {
    private String gyarto;
    @OneToOne
    private GumiMeretekEntity meret;
    private Float ar;
    private String evszak;
    private String allapot;
    private Integer mennyisegRaktarban;

    public String getGyarto() {
        return gyarto;
    }

    public void setGyarto(String gyarto) {
        this.gyarto = gyarto;
    }

    public GumiMeretekEntity getMeret() {
        return meret;
    }

    public void setMeret(GumiMeretekEntity meret) {
        this.meret = meret;
    }

    public Float getAr() {
        return ar;
    }

    public void setAr(Float ar) {
        this.ar = ar;
    }

    public String getEvszak() {
        return evszak;
    }

    public void setEvszak(String evszak) {
        this.evszak = evszak;
    }

    public String getAllapot() {
        return allapot;
    }

    public void setAllapot(String allapot) {
        this.allapot = allapot;
    }

    public Integer getMennyisegRaktarban() {
        return mennyisegRaktarban;
    }

    public void setMennyisegRaktarban(Integer mennyisegRaktarban) {
        this.mennyisegRaktarban = mennyisegRaktarban;
    }
}
