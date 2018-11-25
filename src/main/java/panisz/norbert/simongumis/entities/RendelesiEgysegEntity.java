package panisz.norbert.simongumis.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class RendelesiEgysegEntity extends BaseEntity {
    private int termek_id;
    private int mennyiseg;
}
