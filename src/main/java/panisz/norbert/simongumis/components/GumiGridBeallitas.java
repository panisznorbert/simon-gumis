package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import panisz.norbert.simongumis.entities.GumikEntity;

class GumiGridBeallitas {
    static void gumiGridBeallitas(Grid<GumikEntity> grid) {
        grid.addColumn(GumikEntity::getGyarto, "Gyártó").setHeader("Gyártó");
        grid.addColumn(GumikEntity::getMeret, "Méret").setHeader("Méret");
        grid.addColumn(GumikEntity::getEvszak, "Évszak").setHeader("Évszak");
        grid.addColumn(GumikEntity::getAllapot, "Állapot").setHeader("Állapot");
        grid.addColumn(TemplateRenderer.<GumikEntity> of("<div>[[item.ar]] Ft</div>")
                .withProperty("ar", GumikEntity::getAr), "ar").setHeader("Ár");
        grid.addColumn(TemplateRenderer.<GumikEntity> of("<div>[[item.raktarkeszlet]] db</div>")
                .withProperty("raktarkeszlet", GumikEntity::getMennyisegRaktarban), "raktarkeszlet").setHeader("Raktarkeszlet");
    }
}
