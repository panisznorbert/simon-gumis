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

    @Override
    public String toString() {
        return szelesseg + "/" + profil + "/R/" + felni;
    }

}
