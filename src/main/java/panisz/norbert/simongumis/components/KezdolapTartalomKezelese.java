package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;
import panisz.norbert.simongumis.entities.KezdolapTartalomEntity;
import panisz.norbert.simongumis.services.KezdolapTartalomService;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.List;

@UIScope
@Component
public class KezdolapTartalomKezelese extends VerticalLayout {

    List<KezdolapTartalomEntity> kezdolapTartalomEntities;

    public KezdolapTartalomKezelese(KezdolapTartalomService kezdolapTartalomService){
        Label cim = new Label("Kezdőoldal tartalmának módosítása");
        kezdolapTartalomEntities=kezdolapTartalomService.egyebTartalom();

        if(kezdolapTartalomEntities != null){
            Collections.sort(kezdolapTartalomEntities);
            for(KezdolapTartalomEntity kezdolapTartalomEntity:kezdolapTartalomEntities){
                ujsor(kezdolapTartalomEntity);
            }
        }
    }


    private void ujsor(KezdolapTartalomEntity kezdolapTartalomEntity){
        HorizontalLayout meglevo = new HorizontalLayout();
        HorizontalLayout gombsor = new HorizontalLayout();

        Button torol = new Button("Töröl");
        Button ment = new Button("Ment");

        if(kezdolapTartalomEntity.getKep() != null){
            Image kep = new Image(String.valueOf(new ByteArrayInputStream(kezdolapTartalomEntity.getKep())), "");
            kep.setWidth("20%");
            meglevo.add(kep);
        }

        if(kezdolapTartalomEntity.getLeiras() != null){
            TextField leiras = new TextField();
            leiras.setValue(kezdolapTartalomEntity.getLeiras());
            meglevo.add(leiras);
        }

        gombsor.add(torol, ment);
        add(meglevo, gombsor);

    }

    private void meglevoModositasa(){

    }

    private void ujHozzaadasa(){

    }
}
