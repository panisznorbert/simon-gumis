package panisz.norbert.simongumis.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "ugyfelek")
public class UgyfelEntity extends BaseEntity {
    private String nev;
    private String telefon;
    private String email;

    @Override
    public String toString() {
        return nev + ", " + telefon + ", " + email;
    }
}
