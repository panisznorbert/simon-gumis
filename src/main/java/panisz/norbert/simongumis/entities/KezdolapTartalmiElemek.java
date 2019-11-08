package panisz.norbert.simongumis.entities;

public enum KezdolapTartalmiElemek {
    SZOROLAP, TARTALOM;

    @Override
    public String toString() {
        switch(this){
            case SZOROLAP: return "Szórólap";
            default: return "Egyéb tartalom";
        }

    }
}
