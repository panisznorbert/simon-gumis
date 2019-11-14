package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

class KezdolapSorok extends HorizontalLayout {

    KezdolapSorok(byte[] kepAdat, String leiras){
        HorizontalLayout tartalom = new HorizontalLayout();
        tartalom.setWidth("900px");

        if(kepAdat != null && leiras != null){

            Image kep = kepBetolt(kepAdat);
            kep.setWidth("20%");
            tartalom.add(kep);
            TextArea szoveg = new TextArea();
            szoveg.setSizeFull();
            szoveg.setReadOnly(true);
            szoveg.setValue(leiras);
            VerticalLayout szovegmezo = new VerticalLayout(szoveg);
            szovegmezo.setAlignItems(Alignment.CENTER);
            tartalom.add(szovegmezo);
            add(tartalom);
        }
        if(kepAdat != null && leiras == null){
            Image kep = kepBetolt(kepAdat);
            kep.setWidth("300px");
            this.setWidth("900px");
            add(kep);
        }
        if(kepAdat == null && leiras != null){
            TextArea szoveg = new TextArea();
            szoveg.setReadOnly(true);
            szoveg.setValue(leiras);
            szoveg.setWidth("900px");
            add(szoveg);
        }

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
