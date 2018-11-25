package panisz.norbert.simongumis.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class AutoGumikEntity extends BaseEntity {
    private int auto_id;
    private int gumi_id;
}
