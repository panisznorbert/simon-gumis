package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import panisz.norbert.simongumis.entities.GumikEntity;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

class GumiGrid extends Grid<GumikEntity>{

    GumiGrid(){
        this.getClassNames().add("gumitabla");

        this.addColumn(new ComponentRenderer<>(GumiGrid::kepBetolt)).setHeader("").setTextAlign(ColumnTextAlign.CENTER).setWidth("110px");

        this.addColumn(GumikEntity::getGyarto, "Gyártó").setHeader("Gyártó");
        this.addColumn(GumikEntity::getMeret, "Méret").setHeader("Méret").setWidth("120px");
        this.addColumn(GumikEntity::getEvszak, "Évszak").setHeader("Évszak").setWidth("120px");
        this.addColumn(GumikEntity::getAllapot, "Állapot").setHeader("Állapot");
        this.addColumn(TemplateRenderer.<GumikEntity> of("<div>[[item.ar]] Ft</div>")
                .withProperty("ar", GumikEntity::getAr), "ar").setHeader("Ár");
        this.addColumn(TemplateRenderer.<GumikEntity> of("<div>[[item.raktarkeszlet]] db</div>")
                .withProperty("raktarkeszlet", GumikEntity::getMennyisegRaktarban), "raktarkeszlet").setHeader("Elérhető");
    }

    private static Component kepBetolt(GumikEntity gumikEntity){
        if(gumikEntity.getKep() != null){
            StreamResource streamResource = new StreamResource("isr", new InputStreamFactory() {
                @Override
                public InputStream createInputStream() {
                    return new ByteArrayInputStream(gumikEntity.getKep());
                }
            });
            return new Image(streamResource, "");
        }else{
            return new Icon(VaadinIcon.BAN);
        }

    }
}
