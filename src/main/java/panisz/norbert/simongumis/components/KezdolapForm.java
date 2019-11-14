package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;
import panisz.norbert.simongumis.entities.KezdolapTartalomEntity;
import panisz.norbert.simongumis.services.KezdolapTartalomService;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@UIScope
@Component
public class KezdolapForm extends VerticalLayout {

    private VerticalLayout alap = new VerticalLayout();

    public KezdolapForm(KezdolapTartalomService kezdolapTartalomService){
        szorolapBetoltes(kezdolapTartalomService);
        tovabbiBetoltes(kezdolapTartalomService);

        alap.setAlignItems(Alignment.CENTER);

        this.add(alap);
    }

    private void szorolapBetoltes(KezdolapTartalomService kezdolapTartalomService){
        KezdolapTartalomEntity kezdolapTartalomEntity = kezdolapTartalomService.aktualisSzorolap();

        if(kezdolapTartalomEntity != null){
            Image szorolap = kepBetolt(kezdolapTartalomEntity);
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
        ujTartalom.setAlignItems(Alignment.CENTER);
        for(KezdolapTartalomEntity kezdolapTartalomEntity:kezdolapTartalomEntityList){
            ujTartalom.add(new KezdolapSorok(kezdolapTartalomEntity.getKep(), kezdolapTartalomEntity.getLeiras()));

        }
        alap.add(ujTartalom);

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
