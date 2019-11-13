package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;
import panisz.norbert.simongumis.entities.KezdolapTartalomEntity;
import panisz.norbert.simongumis.services.KezdolapTartalomService;

import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@UIScope
@Component
public class KezdolapForm extends VerticalLayout {

    private VerticalLayout alap = new VerticalLayout();

    private Image szorolap;

    public KezdolapForm(KezdolapTartalomService kezdolapTartalomService){
        szorolapBetoltes(kezdolapTartalomService);
        tovabbiBetoltes(kezdolapTartalomService);

        alap.setAlignItems(Alignment.CENTER);


        this.add(alap);
    }

    private void szorolapBetoltes(KezdolapTartalomService kezdolapTartalomService){
        KezdolapTartalomEntity kezdolapTartalomEntity = kezdolapTartalomService.aktualisSzorolap();

        if(kezdolapTartalomEntity != null){
            StreamResource streamResource = new StreamResource("isr", new InputStreamFactory() {
                @Override
                public InputStream createInputStream() {
                    return new ByteArrayInputStream(kezdolapTartalomEntity.getKep());
                }
            });

            szorolap = new Image(streamResource, "");
            szorolap.setWidth(kezdolapTartalomEntity.getKepMeret());
            alap.add(szorolap);
        }

    }


    private void tovabbiBetoltes(KezdolapTartalomService kezdolapTartalomService){
        List<KezdolapTartalomEntity> kezdolapTartalomEntityList = kezdolapTartalomService.egyebTartalom();

        if(kezdolapTartalomEntityList == null){
            return;

        }

        Collections.sort(kezdolapTartalomEntityList);
        VerticalLayout ujTartalom = new VerticalLayout();
        for(KezdolapTartalomEntity kezdolapTartalomEntity:kezdolapTartalomEntityList){
            HorizontalLayout ujSor = new HorizontalLayout();
            ujSor.setAlignItems(Alignment.BASELINE);
            if(kezdolapTartalomEntity.getKep() != null){
                Image kep = new Image(String.valueOf(new ByteArrayInputStream(kezdolapTartalomEntity.getKep())), "");
                kep.setWidth(kezdolapTartalomEntity.getKepMeret() + "%");
                ujSor.add(kep);
            }
            if(kezdolapTartalomEntity.getLeiras() != null){
                TextArea leiras = new TextArea();
                leiras.setValue(kezdolapTartalomEntity.getLeiras());
                ujSor.add(leiras);
            }
            ujTartalom.add(ujSor);
        }
        alap.add(ujTartalom);
    }
}
