package panisz.norbert.simongumis.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class RendelesekEntity extends BaseEntity {
    private int ugyfel_id;
    private List<RendelesiEgysegEntity> rendelesek;
    private int vegosszeg;
    private String statusz;
}
