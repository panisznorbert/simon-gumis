package panisz.norbert.simongumis.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "adminok")
public class AdminEntity extends BaseEntity {
    private String nev;
    private String jelszo;
    private String session;
}
