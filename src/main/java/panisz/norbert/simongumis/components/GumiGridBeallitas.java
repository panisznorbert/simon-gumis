package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.grid.Grid;
import panisz.norbert.simongumis.entities.GumikEntity;

public class GumiGridBeallitas {
    static void gumiGridBeallitas(Grid<GumikEntity> grid) {
        grid.addColumn(GumikEntity::getGyarto, "Gyártó").setHeader("Gyártó");
        grid.addColumn(GumikEntity::getMeret, "Méret").setHeader("Méret");
        grid.addColumn(GumikEntity::getEvszak, "Évszak").setHeader("Évszak");
        grid.addColumn(GumikEntity::getAllapot, "Állapot").setHeader("Állapot");
        grid.addColumn(GumikEntity::getAr, "Ár").setHeader("Ár");
        grid.addColumn(GumikEntity::getMennyisegRaktarban, "Raktárkészlet").setHeader("Raktáron (db)");
    }
}
