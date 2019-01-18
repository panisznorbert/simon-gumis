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
    private Float ar;
    private String evszak;
    private String allapot;
    private Integer mennyisegRaktarban;
/**
    public GumikEntity(String gyarto, String meret, Float ar, String evszak, String allapot, Integer mennyisegRaktarban) {
        this.gyarto = gyarto;
        this.meret = meret;
        this.ar = ar;
        this.evszak = evszak;
        this.allapot = allapot;
        this.mennyisegRaktarban = mennyisegRaktarban;
    }
 */
}
