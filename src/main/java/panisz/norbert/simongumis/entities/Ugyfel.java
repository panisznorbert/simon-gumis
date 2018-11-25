package panisz.norbert.simongumis.entities;

import lombok.Data;

@Data
public class Ugyfel extends BaseEntity {
    private String nev;
    private String telefon;
    private String email;
}
