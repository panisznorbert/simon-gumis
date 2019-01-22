package panisz.norbert.simongumis.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class GumiMeretekEntity extends BaseEntity {
    Integer szelesseg;
    Integer profil;
    Integer felni;

    public Integer getSzelesseg() {
        return szelesseg;
    }

    public void setSzelesseg(Integer szelesseg) {
        this.szelesseg = szelesseg;
    }

    public Integer getProfil() {
        return profil;
    }

    public void setProfil(Integer profil) {
        this.profil = profil;
    }

    public Integer getFelni() {
        return felni;
    }

    public void setFelni(Integer felni) {
        this.felni = felni;
    }

    @Override
    public String toString() {
        return szelesseg + "/" + profil + "/R/" + felni;
    }

}
