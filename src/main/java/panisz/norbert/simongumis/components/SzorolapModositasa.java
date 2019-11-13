package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;
import panisz.norbert.simongumis.entities.KezdolapTartalmiElemek;
import panisz.norbert.simongumis.entities.KezdolapTartalomEntity;
import panisz.norbert.simongumis.services.KezdolapTartalomService;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@UIScope
@Component
public class SzorolapModositasa extends VerticalLayout {

    private final static Logger LOGGER = Logger.getLogger(SzorolapModositasa.class.getName());

    private MemoryBuffer memoryBuffer = new MemoryBuffer();
    private Upload imgUpload;

    private Notification hibaAblak;

    private HorizontalLayout feltoltes = new HorizontalLayout();

    public SzorolapModositasa(KezdolapTartalomService kezdolapTartalomService){
        Label cim = new Label("Kezdőképernyőn lévő szórólap módosítása:");

        uploadInit();

        imgUpload.addStartedListener(e -> {
            System.out.println("Handling upload of " + e.getFileName()
                    + " (" + e.getContentLength() + " bytes) started");
        });

        imgUpload.addFinishedListener(e -> {
            System.out.println("Handling upload of " + e.getFileName()
                    + " (" + e.getContentLength() + " bytes) finished");
        });

        imgUpload.addSucceededListener(e -> {
            System.out.println("Succeeded Upload of " + e.getFileName());
        });

        this.setAlignItems(Alignment.CENTER);
        feltoltes.setAlignItems(Alignment.BASELINE);

        Button ment = new Button("Ment");
        ment.addClickListener(e -> mentes(kezdolapTartalomService));

        Button eltavolit = new Button("Eltávolít");
        eltavolit.addClickListener(e -> eltavolitas(kezdolapTartalomService));

        HorizontalLayout gombsor = new HorizontalLayout(ment, eltavolit);
        add(cim, feltoltes, gombsor);
    }

    private void uploadInit(){
        feltoltes.removeAll();
        imgUpload = new Upload(memoryBuffer);
        imgUpload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
        imgUpload.setDropLabel(new Label("Húzza ide a fájlt"));
        imgUpload.setUploadButton(new Icon(VaadinIcon.FILE_ADD));
        feltoltes.add(imgUpload);
    }

    private void mentes(KezdolapTartalomService kezdolapTartalomService){

        String uzenet = "A kép frissítése sikeresen megtörtént.";

        KezdolapTartalomEntity kezdolapTartalomEntity = kezdolapTartalomService.aktualisSzorolap();
        KezdolapTartalomEntity ujSzorolap = new KezdolapTartalomEntity();
        ujSzorolap.setPozicio(LocalDateTime.now());

        if(memoryBuffer.getFileName().isEmpty()){
            uzenet = "Nincs kiválasztva kép";
            hibaAblak = new Hibajelzes(uzenet);
            hibaAblak.open();
            LOGGER.info("nincs képnév");
            return;
        }
        if(kezdolapTartalomEntity == null){
            ujSzorolap.setKepMeret("40%");
            ujSzorolap.setMegnevezes(KezdolapTartalmiElemek.SZOROLAP);

        }else{
            ujSzorolap = kezdolapTartalomEntity;
        }

        try{
            ujSzorolap.setKep(memoryBuffer.getInputStream().readAllBytes());
            kezdolapTartalomService.ment(ujSzorolap);
        }catch (Exception ex) {
            uzenet = "A kép mentése sikertelen";
        }

        uploadInit();
        hibaAblak = new Hibajelzes(uzenet);
        hibaAblak.open();
    }

    private void eltavolitas(KezdolapTartalomService kezdolapTartalomService){
        KezdolapTartalomEntity kezdolapTartalomEntity = kezdolapTartalomService.aktualisSzorolap();
        if(kezdolapTartalomEntity != null){
            kezdolapTartalomService.torol(kezdolapTartalomEntity);
            hibaAblak = new Hibajelzes("Szórólap sikeresen eltávolítva.");
            hibaAblak.open();
        }
    }
}
