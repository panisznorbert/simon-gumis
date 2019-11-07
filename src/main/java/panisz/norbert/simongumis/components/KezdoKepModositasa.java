package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.button.Button;
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

    public KezdoKepModositasa(){
        Button ment = new Button("Ment");


        MemoryBuffer memoryBuffer = new MemoryBuffer();
        Upload imgUpload = new Upload(memoryBuffer);


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

            try {

                File output = new File("src/main/resources/META-INF/resources/images/borito.png");
                FileUtils.copyInputStreamToFile(memoryBuffer.getInputStream(), output);

            } catch (Exception ex) {
                LOGGER.info("upload error: " + ex.getMessage());


            }
        });

        HorizontalLayout feltoltes = new HorizontalLayout();
        feltoltes.add(imgUpload, ment);

        add(feltoltes);
    }
}
