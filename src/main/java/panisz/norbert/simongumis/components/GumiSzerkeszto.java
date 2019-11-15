package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import panisz.norbert.simongumis.entities.GumiMeretekEntity;
import panisz.norbert.simongumis.entities.GumikEntity;
import panisz.norbert.simongumis.entities.KezdolapTartalomEntity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


class GumiSzerkeszto extends HorizontalLayout {

    private TextField gyarto = new TextField("Gyártó");
    private TextField meret1 = new TextField("Méret-szélesség");
    private TextField meret2 = new TextField("Méret-profil arány");
    private TextField meret3 = new TextField("Méret-felni átmérő");
    private TextField ar = new TextField("Ár");
    private ComboBox<String> evszak = new ComboBox<>("Évszak", "Téli", "Nyári", "Négyévszakos");
    private ComboBox<String> allapot = new ComboBox<>("Állapot", "Új","Használt");
    private TextField darab  = new TextField("Raktárkészlet");
    private MemoryBuffer memoryBuffer = new MemoryBuffer();
    private Upload imgUpload;


    GumiSzerkeszto(GumikEntity gumikEntity){

        ar.setPattern("\\d*(\\.\\d*)?");
        ar.setPreventInvalidInput(true);
        ar.setSuffixComponent(new Span("Ft"));

        darab.setPattern("[0-9]*");
        darab.setPreventInvalidInput(true);
        darab.setSuffixComponent(new Span("Db"));

        gyarto.setRequired(true);
        ar.setRequired(true);
        meret1.setRequired(true);
        meret2.setRequired(true);
        meret3.setRequired(true);
        evszak.setRequired(true);
        allapot.setRequired(true);
        darab.setRequired(true);

        gyarto.setValue(gumikEntity.getGyarto());
        meret1.setValue(gumikEntity.getMeret().getSzelesseg().toString());
        meret2.setValue(gumikEntity.getMeret().getProfil().toString());
        meret3.setValue(gumikEntity.getMeret().getFelni().toString());
        evszak.setValue(gumikEntity.getEvszak());
        allapot.setValue(gumikEntity.getAllapot());
        ar.setValue(gumikEntity.getAr().toString());
        darab.setValue(gumikEntity.getMennyisegRaktarban().toString());

        imgUpload = new Upload(memoryBuffer);
        imgUpload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
        imgUpload.setDropLabel(new Label("Húzza ide a fájlt"));
        imgUpload.setUploadButton(new Icon(VaadinIcon.FILE_ADD));
        imgUpload.setWidth("200px");

        Icon icon = new Icon(VaadinIcon.BAN);
        icon.setSize("100px");

        VerticalLayout kerek = new VerticalLayout();
        kerek.setAlignItems(Alignment.CENTER);

        Image gumikep;

        VerticalLayout kepfeltoltes = new VerticalLayout();
        kepfeltoltes.setAlignItems(Alignment.CENTER);
        if(gumikEntity.getKep() != null){
            StreamResource streamResource = new StreamResource("isr", new InputStreamFactory() {
                @Override
                public InputStream createInputStream() {
                    return new ByteArrayInputStream(gumikEntity.getKep());
                }
            });
            gumikep = new Image(streamResource, "");
            gumikep.setWidth("300px");
            kerek.add(gumikep);
        }else{
            kerek.add(icon);
        }
        kepfeltoltes.add(kerek);


        imgUpload.addSucceededListener(e -> {
            System.out.println("Succeeded Upload of " + e.getFileName());
                kerek.removeAll();
                kerek.add(ujKep(memoryBuffer));
        });

        kepfeltoltes.add(imgUpload);
        kepfeltoltes.setWidth("300px");
        kepfeltoltes.setAlignItems(Alignment.CENTER);

        VerticalLayout oszlop1 = new VerticalLayout(gyarto, meret2, evszak, ar);
        VerticalLayout oszlop2 = new VerticalLayout(meret1, meret3, allapot, darab);
        oszlop1.setWidth("200px");
        oszlop2.setWidth("200px");
        add(oszlop1, oszlop2, kepfeltoltes);
    }

    GumikEntity beallit(GumikEntity gumikEntity){
            gumikEntity.setGyarto(gyarto.getValue());
            GumiMeretekEntity meret = gumikEntity.getMeret();
            meret.setSzelesseg(Integer.valueOf(meret1.getValue()));
            meret.setProfil(Integer.valueOf(meret2.getValue()));
            meret.setFelni(Integer.valueOf(meret3.getValue()));
            gumikEntity.setMeret(meret);
            gumikEntity.setEvszak(evszak.getValue());
            gumikEntity.setAllapot(allapot.getValue());
            gumikEntity.setAr(Integer.valueOf(ar.getValue()));
            gumikEntity.setMennyisegRaktarban(Integer.valueOf(darab.getValue()));


        if(!memoryBuffer.getFileName().isEmpty()){
            try{
                gumikEntity.setKep(memoryBuffer.getInputStream().readAllBytes());
            }catch (Exception ex){
                Notification hibaAblak = new Hibajelzes("A kép mentése sikertelen");
                hibaAblak.open();
            }
        }

        return gumikEntity;
    }

    String validacio() {
        return GumikKezeleseForm.getString(gyarto, evszak, allapot, ar, darab, meret1, meret2, meret3);
    }

    private Image ujKep(MemoryBuffer memoryBuffer){
            StreamResource streamResource = new StreamResource("isr", new InputStreamFactory() {
                @Override
                public InputStream createInputStream() {
                    try {
                        return new ByteArrayInputStream(memoryBuffer.getInputStream().readAllBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            });
            Image kep = new Image(streamResource, "");
            kep.setWidth("300px");
            return kep;
    }

}
