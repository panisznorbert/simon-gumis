package panisz.norbert.simungumis.entities;

import lombok.Data;

import java.util.Date;

@Data
public class Foglalas extends BaseEntity {
    private Date datum;
    private int ugyfel_id;
    private String statusz;
    private String megjegyzes;

}
