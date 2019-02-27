package panisz.norbert.simongumis.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "gumi_meretek")
public class GumiMeretekEntity extends BaseEntity {
    Integer szelesseg;
    Integer profil;
    Integer felni;

    @Override
    public String toString() {
        return szelesseg + "/" + profil + "/R/" + felni;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GumiMeretekEntity that = (GumiMeretekEntity) o;
        return Objects.equals(szelesseg, that.szelesseg) &&
                Objects.equals(profil, that.profil) &&
                Objects.equals(felni, that.felni);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), szelesseg, profil, felni);
    }
}
