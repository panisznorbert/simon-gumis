package panisz.norbert.simungumis.entities;

import lombok.Data;

@Data
public class Gumik extends BaseEntity {
    private String gyarto;
    private String meret;
    private int ar;
    private String evszak;
    private String allapot;
    private int mennyiseg;
}
