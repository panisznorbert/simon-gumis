package panisz.norbert.simongumis.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class GumikEntity extends BaseEntity {
    private String gyarto;
    private String meret;
    private float ar;
    private String evszak;
    private String allapot;
    private int mennyisegRaktarban;
}
