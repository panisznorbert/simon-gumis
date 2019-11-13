package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;
import panisz.norbert.simongumis.entities.KezdolapTartalmiElemek;
import panisz.norbert.simongumis.entities.KezdolapTartalomEntity;
import panisz.norbert.simongumis.services.KezdolapTartalomService;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@UIScope
@Component
public class KezdolapTartalomKezelese extends VerticalLayout {

    private KezdolapTartalomService alapKezdolapTartalomService;

    private List<KezdolapTartalomEntity> kezdolapTartalomEntities;

    private Label cim = new Label("Kezdőoldal tartalmának módosítása:");

    private Label ujCim = new Label("Új leírás hozzáadása:");

    public KezdolapTartalomKezelese(KezdolapTartalomService kezdolapTartalomService){
        alapKezdolapTartalomService = kezdolapTartalomService;
        kezdolapTartalomEntities=kezdolapTartalomService.egyebTartalom();
        this.setAlignItems(Alignment.CENTER);
        elemfelveteleInit();


    }

    private HorizontalLayout ujElem(){

        MemoryBuffer memoryBuffer = new MemoryBuffer();
        Upload imgUpload = new Upload(memoryBuffer);

        imgUpload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
        imgUpload.setDropLabel(new Label("Húzza ide a fájlt"));
        imgUpload.setUploadButton(new Icon(VaadinIcon.FILE_ADD));
        imgUpload.setWidth("25%");
        TextArea leiras = new TextArea();
        leiras.setWidth("60%");
        Button ment = new Button("Hozzáad");
        ment.setWidth("15%");

        ment.addClickListener(e -> ujElemMentese(memoryBuffer, leiras.getValue()));

        return new HorizontalLayout(imgUpload, leiras, ment);
    }

    private void ujElemMentese(MemoryBuffer memoryBuffer, String leiras){
        Notification hibaAblak;
        KezdolapTartalomEntity ujTartalom = new KezdolapTartalomEntity();
        ujTartalom.setMegnevezes(KezdolapTartalmiElemek.TARTALOM);
        ujTartalom.setPozicio(LocalDateTime.now());

        if(!leiras.isEmpty()){
            ujTartalom.setLeiras(leiras);
            ujTartalom.setKepMeret("15%");
        }else{
            ujTartalom.setKepMeret("40%");
        }

        if(!memoryBuffer.getFileName().isEmpty()){
            try{
                ujTartalom.setKep(memoryBuffer.getInputStream().readAllBytes());
            }catch (Exception ex){
                hibaAblak = new Hibajelzes("A kép mentése sikertelen");
                hibaAblak.open();
            }
        }

        try{
            alapKezdolapTartalomService.ment(ujTartalom);
        }catch(Exception ex){
            hibaAblak = new Hibajelzes("A mentés sikertelen");
            hibaAblak.open();
        }

        elemfelveteleInit();
    }

    private void elemfelveteleInit(){
        HorizontalLayout alap1 = ujElem();
        alap1.setWidth("900px");
        VerticalLayout alap2 = tartalomFeltoltes(kezdolapTartalomEntities);
        removeAll();
        add(cim, ujCim, alap1, alap2);
    }

    private VerticalLayout tartalomFeltoltes(List<KezdolapTartalomEntity> kezdolapTartalomEntities){
        VerticalLayout alap = new VerticalLayout();
        alap.setAlignItems(Alignment.CENTER);
        Label cim = new Label("Meglévő leírások:");
        add(cim);
        Collections.sort(kezdolapTartalomEntities);
        int sorszam = 1;
        for(KezdolapTartalomEntity kezdolapTartalomEntity:kezdolapTartalomEntities){
            HorizontalLayout ujTartalom = new HorizontalLayout();
            ujTartalom.setAlignItems(Alignment.CENTER);
            ujTartalom.add(new Label(Integer.toString(sorszam)));
            if(kezdolapTartalomEntity.getKep() != null && kezdolapTartalomEntity.getLeiras() != null){
                ujTartalom.add(new KezdolapSorok(kezdolapTartalomEntity.getKep(), kezdolapTartalomEntity.getLeiras()));
            }
            if(kezdolapTartalomEntity.getKep() != null && kezdolapTartalomEntity.getLeiras() == null){
                ujTartalom.add(new KezdolapSorok(kezdolapTartalomEntity.getKep()));
            }
            if(kezdolapTartalomEntity.getKep() == null && kezdolapTartalomEntity.getLeiras() != null){
                ujTartalom.add(new KezdolapSorok(kezdolapTartalomEntity.getLeiras()));
            }
            sorszam++;
            Button torol = new Button("Töröl");
            ujTartalom.add(torol);
            alap.add(ujTartalom);
        }
        return alap;
    }


    private void sorTorlese(KezdolapTartalomEntity kezdolapTartalomEntity, VerticalLayout sor){
        alapKezdolapTartalomService.torol(kezdolapTartalomEntity);
        remove(sor);
    }


}
