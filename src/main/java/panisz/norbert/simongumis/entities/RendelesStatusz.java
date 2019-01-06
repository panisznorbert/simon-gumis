package panisz.norbert.simongumis.entities;

import javax.persistence.Embeddable;

@Embeddable
public enum RendelesStatusz {
    KOSÁRBAN, MEGRENDELVE, VISSZAIGAZOLVA, RENDEZVE, TÖRÖLVE, ÜGYFÉL_KÉRÉSÉRE_TÖRÖLVE
}
