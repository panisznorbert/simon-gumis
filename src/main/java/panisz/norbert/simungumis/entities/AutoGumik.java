package panisz.norbert.simungumis.entities;

import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class AutoGumik extends BaseEntity {
    private int auto_id;
    private int gumi_id;
}
