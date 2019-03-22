package panisz.norbert.simongumis.entities;

public enum RendelesStatusz {
    KOSARBAN, MEGRENDELVE, ATVETELRE_VAR, RENDEZVE, TOROLVE;

    @Override
    public String toString() {
        switch(this){
            case MEGRENDELVE: return "Megrendelve";
            case ATVETELRE_VAR: return "Átvételre vár";
            case RENDEZVE: return "Rendezve";
            case TOROLVE: return "Törölve";
            default: return "Kosárban";
        }

    }
}
