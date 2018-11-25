package panisz.norbert.simungumis.entities;

import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class Admin extends BaseEntity {
    private String nev;
    private String jelszo;
}
