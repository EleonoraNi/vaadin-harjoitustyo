package fi.harjoitustyo.vaadin_app.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import fi.harjoitustyo.vaadin_app.entity.Liikuntatunti;
import fi.harjoitustyo.vaadin_app.entity.Liikkuja;
import fi.harjoitustyo.vaadin_app.service.LiikuntatuntiService;
import fi.harjoitustyo.vaadin_app.service.LiikkujaService;

@Route(value = "Ilmoittautuminen", layout = MainLayout.class)
@PageTitle("Ilmoittautuminen liikuntatunnille")
public class LiikkujaIlmoittautuminenView extends VerticalLayout {

    private final LiikkujaService liikkujaService;
    private final LiikuntatuntiService liikuntatuntiService;

    private ComboBox<Liikkuja> liikkujaCombo = new ComboBox<>("Valitse liikkuja");
    private Grid<Liikuntatunti> tuntiGrid = new Grid<>(Liikuntatunti.class, false);

    public LiikkujaIlmoittautuminenView(
            LiikkujaService liikkujaService,
            LiikuntatuntiService liikuntatuntiService) {

        this.liikkujaService = liikkujaService;
        this.liikuntatuntiService = liikuntatuntiService;

        liikkujaCombo.setItems(liikkujaService.findAll());
        liikkujaCombo.setItemLabelGenerator(l -> l.getEtunimi() + " " + l.getSukunimi());

        tuntiGrid.addColumn(Liikuntatunti::getNimi).setHeader("Tunti");
        tuntiGrid.addColumn(Liikuntatunti::getTyyppi).setHeader("Tyyppi");

        tuntiGrid.addComponentColumn(tunti -> {
            Button ilmoittaudu = new Button("Ilmoittaudu");
            ilmoittaudu.addClickListener(e -> {
                try {
                    liikkujaService.ilmoittauduTunnille(
                            liikkujaCombo.getValue(), tunti);
                    Notification.show("Ilmoittautuminen onnistui");
                } catch (Exception ex) {
                    Notification.show(ex.getMessage(), 3000,
                            Notification.Position.MIDDLE);
                }
            });
            return ilmoittaudu;
        });

        liikkujaCombo.addValueChangeListener(e -> tuntiGrid.setItems(
                liikuntatuntiService.findAllSortedByAlkuaika()));

        add(
                new H2("Liikkujan ilmoittautuminen"),
                liikkujaCombo,
                tuntiGrid);
    }
}