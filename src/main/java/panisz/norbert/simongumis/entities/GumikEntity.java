package panisz.norbert.simongumis.entities;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import lombok.Data;
import panisz.norbert.simongumis.components.Hibajelzes;

import javax.persistence.*;
import java.util.Comparator;
import java.util.Objects;

@Data
@Entity
@Table(name = "gumik")
public class GumikEntity extends BaseEntity implements Comparable<GumikEntity>{
    private String gyarto;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    protected GumiMeretekEntity meret;
    private Integer ar;
    private String evszak;
    private String allapot;
    private Integer mennyisegRaktarban;
    @Lob
    private byte[] kep;

    @Override
    public String toString() {
        return gyarto + " " + meret + " " + evszak + " " + allapot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GumikEntity that = (GumikEntity) o;
        return gyarto.equalsIgnoreCase(that.gyarto) &&
                meret.equals(that.meret) &&
                evszak.equals(that.evszak) &&
                allapot.equals(that.allapot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gyarto, meret, evszak, allapot);
    }

    public void beallit (MegrendeltGumikEntity megrendeltGumikEntity, int darab){
        this.setAllapot(megrendeltGumikEntity.getAllapot());
        this.setEvszak(megrendeltGumikEntity.getEvszak());
        this.setGyarto(megrendeltGumikEntity.getGyarto());
        this.setMennyisegRaktarban(darab);
        this.setAr(megrendeltGumikEntity.getAr());
        GumiMeretekEntity gumiMeretekEntity = new GumiMeretekEntity();
        gumiMeretekEntity.setFelni(megrendeltGumikEntity.getMeretFelni());
        gumiMeretekEntity.setProfil(megrendeltGumikEntity.getMeretProfil());
        gumiMeretekEntity.setSzelesseg(megrendeltGumikEntity.getMeretSzelesseg());
        this.setMeret(gumiMeretekEntity);
        this.setKep(megrendeltGumikEntity.getKep());
    }

    public void setKep(byte[] kep) {
        this.kep = kep;
    }

    public void setKep(MemoryBuffer memoryBuffer) {

        if(!memoryBuffer.getFileName().isEmpty()){
            try{
                this.setKep(memoryBuffer.getInputStream().readAllBytes());
            }catch (Exception ex){
                Notification hibaAblak = new Hibajelzes("A kép mentése sikertelen");
                hibaAblak.open();
            }
        }
    }

    @Override
    public int compareTo(GumikEntity gumikEntity) {
        return Comparators.GYARTO.compare(this, gumikEntity);
    }

    public static class Comparators {

        public static Comparator<GumikEntity> GYARTO = new Comparator<GumikEntity>() {
            @Override
            public int compare(GumikEntity o1, GumikEntity o2) {
                return o1.getGyarto().compareTo(o2.getGyarto());
            }
        };
        public static Comparator<GumikEntity> MERET = new Comparator<GumikEntity>() {
            @Override
            public int compare(GumikEntity o1, GumikEntity o2) {
                return o1.getMeret().compareTo(o2.getMeret());
            }
        };
        public static Comparator<GumikEntity> ARNOVEKVO = new Comparator<GumikEntity>() {
            @Override
            public int compare(GumikEntity o1, GumikEntity o2) {
                return o1.getAr().compareTo(o2.getAr());
            }
        };
        public static Comparator<GumikEntity> ARCSOKKENO = new Comparator<GumikEntity>() {
            @Override
            public int compare(GumikEntity o1, GumikEntity o2) {
                return o2.getAr().compareTo(o1.getAr());
            }
        };
    }
}
