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
    private Integer ar;
    private String evszak;
    private String allapot;
    private Integer mennyisegRaktarban;

}
