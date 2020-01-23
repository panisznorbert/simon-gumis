package panisz.norbert.simongumis.components;

import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import panisz.norbert.simongumis.entities.*;
import panisz.norbert.simongumis.services.AdminService;
import panisz.norbert.simongumis.services.GumikService;
import panisz.norbert.simongumis.services.RendelesService;
import java.util.List;

@StyleSheet("gumiktabla.css")
class GumikLap extends VerticalLayout {

    GumikLap(List<GumikEntity> szurtGumik, GumikService gumikService, RendelesService rendelesService, AppLayoutMenuItem kosar, AdminService adminService){
        szurtGumik.sort(GumikEntity.Comparators.GYARTO);
        for (GumikEntity gumi : szurtGumik) {
            this.add(new GumikLapSor(gumi, gumikService, rendelesService, kosar, adminService));
        }
        this.setSizeUndefined();
        this.getStyle().set("align-items", "center");
    }
}
