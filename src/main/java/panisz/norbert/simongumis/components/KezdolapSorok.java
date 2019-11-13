package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class KezdolapSorok extends HorizontalLayout {

    public KezdolapSorok(byte[] kepAdat, String leiras){
        HorizontalLayout tartalom = new HorizontalLayout();
        Image kep = kepBetolt(kepAdat);
        kep.setWidth("20%");
        tartalom.add(kep);
        TextArea szoveg = new TextArea();
        szoveg.setWidth("100%");
        szoveg.setReadOnly(true);
        szoveg.setValue(leiras);
        tartalom.setWidth("900px");
        VerticalLayout szovegmezo = new VerticalLayout(szoveg);
        szovegmezo.setAlignItems(Alignment.CENTER);
        tartalom.add(szovegmezo);
        add(tartalom);

    }

    public KezdolapSorok(byte[] kepAdat){
        Image kep = kepBetolt(kepAdat);
        kep.setSizeFull();
        this.setWidth("40%");
        add(kep);

    }

    public KezdolapSorok(String leiras){
        TextArea szoveg = new TextArea();
        szoveg.setReadOnly(true);
        szoveg.setValue(leiras);
        szoveg.setWidth("900px");
        add(szoveg);

    }

    private Image kepBetolt(byte[] kep){
        StreamResource streamResource = new StreamResource("isr", new InputStreamFactory() {
            @Override
            public InputStream createInputStream() {
                return new ByteArrayInputStream(kep);
            }
        });

        return new Image(streamResource, "");
    }

}
