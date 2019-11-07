package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.spring.annotation.UIScope;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import java.io.File;
import java.util.logging.Logger;

@UIScope
@Component
public class KezdoKepModositasa extends VerticalLayout {

    private final static Logger LOGGER = Logger.getLogger(KezdoKepModositasa.class.getName());

    private MemoryBuffer memoryBuffer = new MemoryBuffer();
    private Upload imgUpload;
    private Button ment = new Button("Ment");

    private HorizontalLayout feltoltes = new HorizontalLayout();

    public KezdoKepModositasa(){
        Label kezdoKepModositasa = new Label("Kezdőképernyőn lévő szórólap módosítása:");

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

        ment.addClickListener(e -> {
            Notification hibaAblak;
            String uzenet = "A kép frissítése sikeresen megtörtént.";;
            try {
                File output = new File("src/main/resources/META-INF/resources/images/borito.png");
                FileUtils.copyInputStreamToFile(memoryBuffer.getInputStream(), output);
                uploadInit();
                feltoltes.remove(imgUpload, ment);
                feltoltes.add(imgUpload, ment);

            } catch (Exception ex) {

                uzenet = "A kép frissítése sikertelen.";
            }
            hibaAblak = new Hibajelzes(uzenet);
            hibaAblak.open();

        });

        this.setAlignItems(Alignment.CENTER);

        feltoltes.setAlignItems(Alignment.BASELINE);

        add(kezdoKepModositasa, feltoltes);
    }

    private void uploadInit(){
        feltoltes.removeAll();
        imgUpload = new Upload(memoryBuffer);
        imgUpload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
        imgUpload.setDropLabel(new Label("Húzza ide a fájlt"));
        imgUpload.setUploadButton(new Button("Tallóz"));
        feltoltes.add(imgUpload, ment);
    }
}
