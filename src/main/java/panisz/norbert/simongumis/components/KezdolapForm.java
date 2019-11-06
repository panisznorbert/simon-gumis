package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

import java.io.InputStream;


@UIScope
@Component
public class KezdolapForm extends VerticalLayout {



    public KezdolapForm(){

        VerticalLayout alap = new VerticalLayout();

        Image szorolap = new Image("images/borito.png", "");

        szorolap.setWidth("60%");

        alap.setAlignItems(Alignment.CENTER);

        alap.add(szorolap);

        FileBuffer fileBuffer = new FileBuffer();
        Upload upload = new Upload(fileBuffer);
        upload.addFinishedListener(e -> {
            InputStream inputStream =
                    fileBuffer.getInputStream();
            // read the contents of the buffered file
            // from inputStream
        });

        this.add(alap, upload);

    }

}
