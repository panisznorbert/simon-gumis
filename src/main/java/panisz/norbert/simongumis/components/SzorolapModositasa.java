package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import panisz.norbert.simongumis.entities.KezdolapTartalmiElemek;
import panisz.norbert.simongumis.entities.KezdolapTartalomEntity;
import panisz.norbert.simongumis.services.KezdolapTartalomService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.logging.Logger;

@StyleSheet("kezdolap.css")
class SzorolapModositasa extends VerticalLayout {

    private final static Logger LOGGER = Logger.getLogger(SzorolapModositasa.class.getName());

    private MemoryBuffer memoryBuffer;

    private Notification hibaAblak;

    private HorizontalLayout feltoltes = new HorizontalLayout();

    private VerticalLayout aktualisSzorolap = new VerticalLayout();

    private Image szorolap;

    SzorolapModositasa(KezdolapTartalomService kezdolapTartalomService){
        Label cim = new Label("Kezdőképernyőn lévő szórólap módosítása:");
        cim.getStyle().set("font-weight", "bold");
        uploadInit();
        this.setAlignItems(Alignment.CENTER);
        feltoltes.setAlignItems(Alignment.BASELINE);
        Button ment = new Button("Ment");
        ment.addClickListener(e -> mentes(kezdolapTartalomService));
        Button eltavolit = new Button("Eltávolít");
        eltavolit.addClickListener(e -> eltavolitas(kezdolapTartalomService));
        HorizontalLayout gombsor = new HorizontalLayout(ment, eltavolit);
        KezdolapTartalomEntity kezdolapTartalomEntity = kezdolapTartalomService.aktualisSzorolap();
        aktualisSzorolap.setId("aktualis-szorolap");

        if(kezdolapTartalomEntity != null){
            szorolap = kepBetolt(kezdolapTartalomEntity);
            szorolap.setWidth(kezdolapTartalomEntity.getKepMeret());
            aktualisSzorolap.setAlignItems(Alignment.CENTER);
            aktualisSzorolap.add(szorolap);
        }

        add(cim, aktualisSzorolap, feltoltes, gombsor);
    }

    private void uploadInit(){
        feltoltes.removeAll();
        memoryBuffer = new MemoryBuffer();
        Upload imgUpload = new Upload(memoryBuffer);
        imgUpload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
        imgUpload.setDropLabel(new Label("Húzza ide a fájlt"));
        imgUpload.setUploadButton(new Icon(VaadinIcon.FILE_ADD));
        feltoltes.add(imgUpload);
    }

    private void mentes(KezdolapTartalomService kezdolapTartalomService){
        KezdolapTartalomEntity kezdolapTartalomEntity = kezdolapTartalomService.aktualisSzorolap();
        KezdolapTartalomEntity ujSzorolap = new KezdolapTartalomEntity();
        ujSzorolap.setPozicio(LocalDateTime.now());

        if(memoryBuffer.getFileName().isEmpty()){
            hibaAblak = new Hibajelzes("Nincs kiválasztva kép");
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
            hibaAblak = new Hibajelzes("A kép mentése sikertelen");
            hibaAblak.open();
        }

        aktualisSzorolap.removeAll();
        szorolap = kepBetolt(ujSzorolap);
        szorolap.setWidth(ujSzorolap.getKepMeret());
        aktualisSzorolap.add(szorolap);
        uploadInit();
    }

    private void eltavolitas(KezdolapTartalomService kezdolapTartalomService){
        KezdolapTartalomEntity kezdolapTartalomEntity = kezdolapTartalomService.aktualisSzorolap();
        uploadInit();
        if(kezdolapTartalomEntity != null){
            kezdolapTartalomService.torol(kezdolapTartalomEntity);
            aktualisSzorolap.removeAll();
            hibaAblak = new Hibajelzes("Szórólap sikeresen eltávolítva.");
            hibaAblak.open();
        }
    }

    private Image kepBetolt(KezdolapTartalomEntity kezdolapTartalomEntity){
        StreamResource streamResource = new StreamResource("isr", new InputStreamFactory() {
            @Override
            public InputStream createInputStream() {
                return new ByteArrayInputStream(kezdolapTartalomEntity.getKep());
            }
        });

        return new Image(streamResource, "");
    }
}
