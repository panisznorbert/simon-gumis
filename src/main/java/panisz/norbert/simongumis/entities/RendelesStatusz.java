package panisz.norbert.simongumis.entities;

import javax.persistence.Embeddable;

@Embeddable
public enum RendelesStatusz {
    KOSARBAN, MEGRENDELVE, VISSZAIGAZOLVA, ATVETELRE_VAR, RENDEZVE, TOROLVE
}
