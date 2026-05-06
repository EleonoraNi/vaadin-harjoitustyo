package fi.harjoitustyo.vaadin_app.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import fi.harjoitustyo.vaadin_app.entity.Liikuntatunti;
import fi.harjoitustyo.vaadin_app.entity.Liikkuja;
import fi.harjoitustyo.vaadin_app.service.LiikuntatuntiService;
import fi.harjoitustyo.vaadin_app.service.LiikkujaService;
import jakarta.annotation.security.RolesAllowed;

import java.time.format.DateTimeFormatter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Route(value = "Ilmoittautuminen", layout = MainLayout.class)
@PageTitle("Ilmoittautuminen liikuntatunnille")
@RolesAllowed({ "ROLE_USER"})
public class LiikkujaIlmoittautuminenView extends VerticalLayout implements BeforeEnterObserver {

    private final LiikkujaService liikkujaService;
    private final LiikuntatuntiService liikuntatuntiService;
    private Liikkuja currentLiikkuja;

    private Grid<Liikuntatunti> tuntiGrid = new Grid<>(Liikuntatunti.class, false);

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("d.M.yyyy 'klo' H:mm");

    public LiikkujaIlmoittautuminenView(
            LiikkujaService liikkujaService,
            LiikuntatuntiService liikuntatuntiService) {

        this.liikkujaService = liikkujaService;
        this.liikuntatuntiService = liikuntatuntiService;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        currentLiikkuja = liikkujaService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Liikkujaa ei löydy käyttäjälle"));

        tuntiGrid.addColumn(Liikuntatunti::getNimi)
                .setHeader("Nimi")
                .setAutoWidth(true);

        tuntiGrid.addColumn(Liikuntatunti::getTyyppi)
                .setHeader("Tyyppi")
                .setAutoWidth(true);

        tuntiGrid.addColumn(tunti -> {
            if (tunti.getAlkuaika() == null || tunti.getLoppuaika() == null) {
                return "";
            }
            String alku = tunti.getAlkuaika().format(DATE_FORMAT);
            String loppu = tunti.getLoppuaika().format(DateTimeFormatter.ofPattern("H:mm"));
            return alku + "–" + loppu;
        }).setHeader("Ajankohta").setAutoWidth(true);

        tuntiGrid.addColumn(tunti -> tunti.getOhjaaja() != null
                ? tunti.getOhjaaja().getNimi()
                : "-").setHeader("Ohjaaja").setAutoWidth(true);

        tuntiGrid.addColumn(tunti -> {
            int ilmoittautuneet = tunti.getLiikkujat() != null
                    ? tunti.getLiikkujat().size()
                    : 0;
            return ilmoittautuneet + " / " + tunti.getKapasiteetti();
        }).setHeader("Ilmoittautuneet").setAutoWidth(true);

        tuntiGrid.addComponentColumn(tunti -> {
            Button ilmoittaudu = new Button("Ilmoittaudu");
            ilmoittaudu.addClickListener(e -> {
                liikkujaService.ilmoittauduTunnille(currentLiikkuja, tunti);
                Notification.show("Ilmoittautuminen onnistui");
                tuntiGrid.getDataProvider().refreshItem(tunti);
            });
            return ilmoittaudu;
        }).setHeader("Toiminnot");

        tuntiGrid.setItems(liikuntatuntiService.findAllSortedByAlkuaika());

        add(
                new H2("Liikkujan ilmoittautuminen"),
                tuntiGrid);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getAuthorities().stream()
                .noneMatch(authority -> authority.getAuthority().equals("ROLE_USER")
                        || authority.getAuthority().equals("ROLE_ADMIN"))) {
            event.rerouteTo(AccessDeniedView.class);
        }
    }
}