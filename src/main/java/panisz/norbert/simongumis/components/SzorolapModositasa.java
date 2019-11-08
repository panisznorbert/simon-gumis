package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
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
import java.util.logging.Logger;

@UIScope
@Component
public class SzorolapModositasa extends VerticalLayout {

    private final static Logger LOGGER = Logger.getLogger(SzorolapModositasa.class.getName());

    private MemoryBuffer memoryBuffer = new MemoryBuffer();
    private Upload imgUpload;

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
        eltavolit.addClickListener(e -> eltavolitas());

        HorizontalLayout gombsor = new HorizontalLayout(ment, eltavolit);
        add(cim, feltoltes, gombsor);
    }

    private void uploadInit(){
        feltoltes.removeAll();
        imgUpload = new Upload(memoryBuffer);
        imgUpload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
        imgUpload.setDropLabel(new Label("Húzza ide a fájlt"));
        imgUpload.setUploadButton(new Button("Tallóz"));
        feltoltes.add(imgUpload);
    }

    private void mentes(KezdolapTartalomService kezdolapTartalomService){
        Notification hibaAblak;
        String uzenet = "A kép frissítése sikeresen megtörtént.";

        KezdolapTartalomEntity kezdolapTartalomEntity = kezdolapTartalomService.aktualisSzorolap();
        KezdolapTartalomEntity ujSzorolap = new KezdolapTartalomEntity();

        if(kezdolapTartalomEntity == null){
            ujSzorolap.setKepMeret("60%");
            ujSzorolap.setMegnevezes(KezdolapTartalmiElemek.SZOROLAP);

        }else{
            ujSzorolap = kezdolapTartalomEntity;
        }

        try{
            ujSzorolap.setKep(memoryBuffer.getInputStream().readAllBytes());
            //kezdolapTartalomService.ment(ujSzorolap);
        }catch (Exception ex) {
            uzenet = "A kép megnyitása sikertelen 1.";
        }

        try{
            //kezdolapTartalomEntity.setKep(memoryBuffer.getInputStream().readAllBytes());
            kezdolapTartalomService.ment(ujSzorolap);
        }catch (Exception ex) {
            uzenet = "A kép megnyitása sikertelen 2.";
        }

        /* Alap megoldás, ami local mappába dolgozik, de ez nem jó mert runtime-ban nem módosul a változás
        try {
            File output = new File("src/main/resources/META-INF/resources/images/borito.png");
            FileUtils.copyInputStreamToFile(memoryBuffer.getInputStream(), output);
            uploadInit();
            feltoltes.remove(imgUpload);
            feltoltes.add(imgUpload);
        } catch (Exception ex) {
            uzenet = "A kép frissítése sikertelen.";
        }
        */

        hibaAblak = new Hibajelzes(uzenet);
        hibaAblak.open();
    }

    private void eltavolitas(){

    }
}
