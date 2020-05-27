package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

@StyleSheet("kezdolap.css")
class KezdolapSorok extends HorizontalLayout {

    KezdolapSorok(byte[] kepAdat, String leiras, boolean kezdolap){

        HorizontalLayout tartalom = new HorizontalLayout();
        tartalom.setId("kezdolap-sorok");

        if(!kezdolap){
            this.setId("kezdolap-tartalom-beallitas");
        }

        if(kepAdat != null && leiras != null){
            Image kep = kepBetolt(kepAdat);
            kep.setId("kep-szoveggel");
            tartalom.add(kep);
            TextArea szoveg = new TextArea();
            szoveg.setSizeFull();
            szoveg.setReadOnly(true);
            szoveg.setValue(leiras);
            VerticalLayout szovegmezo = new VerticalLayout(szoveg);
            szovegmezo.setId("kep-szoveggel-leiras");
            tartalom.add(szovegmezo);
            add(tartalom);
        }
        if(kepAdat != null && leiras == null){
            Image kep = kepBetolt(kepAdat);
            kep.setId("csak-kep");
            add(kep);
        }
        if(kepAdat == null && leiras != null){
            TextArea szoveg = new TextArea();
            szoveg.setId("csak-szoveg");
            szoveg.setReadOnly(true);
            szoveg.setValue(leiras);
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
