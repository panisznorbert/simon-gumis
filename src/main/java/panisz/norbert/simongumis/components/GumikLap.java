package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import panisz.norbert.simongumis.entities.GumikEntity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

public class GumikLap extends VerticalLayout {

    GumikLap(List<GumikEntity> gumik){
        lapfeltoltes(gumik);
        this.setSizeUndefined();
        this.getStyle().set("align-items", "center");
    }

    private void lapfeltoltes(List<GumikEntity> gumik){
        for (GumikEntity gumi : gumik) {
            this.add(ujSor(gumi));
        }
    }

    private HorizontalLayout ujSor(GumikEntity gumi){

        Button gumiKep = kepBetolt(gumi.getKep());
        gumiKep.setId("kep-button");
        gumiKep.addClickListener(e -> sorkivalasztas(gumi.getKep()));

        Label gyarto = new Label(gumi.getGyarto());
        gyarto.setId("gyarto-label");
        Label meret = new Label(gumi.getMeret().toString());
        meret.setId("meret-label");
        Label evszak = new Label(gumi.getEvszak());
        evszak.setId("evszak-label");
        Label allapot = new Label(gumi.getAllapot());
        allapot.setId("allapot-label");

        Label keszlet = new Label(gumi.getMennyisegRaktarban().toString());
        keszlet.setId("keszlet-label");
        Label ar = new Label(gumi.getAr().toString());
        ar.setId("ar-label");

        TextField darabVasarlas = new TextField();
        Button kosarba = new Button("kosárba");

        VerticalLayout kepHelye = new VerticalLayout(gumiKep);
        kepHelye.setSizeUndefined();
        kepHelye.setId("kepHelye");
        VerticalLayout leirasHelye1 = new VerticalLayout(gyarto, meret, evszak, allapot);
        leirasHelye1.setSizeUndefined();
        leirasHelye1.setId("leirasHelye1");
        VerticalLayout leirasHelye2 = new VerticalLayout(keszlet, ar);
        leirasHelye2.setSizeUndefined();
        leirasHelye1.setId("leirasHelye2");
        VerticalLayout vasarlashelye = new VerticalLayout(darabVasarlas, kosarba);
        vasarlashelye.setSizeUndefined();
        leirasHelye2.setId("vasarlashelye");

        HorizontalLayout sor = new HorizontalLayout(kepHelye, leirasHelye1, leirasHelye2, vasarlashelye);
        sor.addClassName("gumik-sor");

        return sor;
    }

    private static Button kepBetolt(byte[] gumikEntity){
        Button kep = new Button();
        if(gumikEntity != null){
            StreamResource streamResource = new StreamResource("isr", new InputStreamFactory() {
                @Override
                public InputStream createInputStream() {
                    return new ByteArrayInputStream(gumikEntity);
                }
            });
            kep.setIcon(new Image(streamResource, ""));
        }else{
            kep.setIcon(new Icon(VaadinIcon.BAN));
            kep.setEnabled(false);
        }
        return kep;
    }

    private void sorkivalasztas(byte[] gumikEntity){
        Dialog kepNagyit = new Dialog();
        Image kep;
        StreamResource streamResource = new StreamResource("isr", new InputStreamFactory() {
            @Override
            public InputStream createInputStream() {
                return new ByteArrayInputStream(gumikEntity);
            }
        });
        kep = new Image(streamResource, "");
        kep.setWidth("400px");
        kepNagyit.setCloseOnOutsideClick(true);
        kepNagyit.add(kep);
        kepNagyit.open();
    }
}
