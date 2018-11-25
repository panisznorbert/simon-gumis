package panisz.norbert.simungumis.entities;

import lombok.Data;

@Data
public class Rendelesek extends BaseEntity {
    private int ugyfel_id;
    private int termek_id;
    private int mennyiseg;
    private int vegosszeg;
    private String statusz;
}
