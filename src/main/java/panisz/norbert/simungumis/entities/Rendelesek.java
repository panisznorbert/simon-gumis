package panisz.norbert.simungumis.entities;

import lombok.Data;

import java.util.List;

@Data
public class Rendelesek extends BaseEntity {
    private int ugyfel_id;
    private List<RendelesiEgyseg> rendelesek;
    private int vegosszeg;
    private String statusz;
}
