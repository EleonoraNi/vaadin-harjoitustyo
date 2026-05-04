package fi.harjoitustyo.vaadin_app.view;

import java.time.format.DateTimeFormatter;

import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import fi.harjoitustyo.vaadin_app.entity.Liikuntatunti;
import fi.harjoitustyo.vaadin_app.service.LiikuntatuntiService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.Authentication;

@PageTitle("Liikuntatunnit")
@Route(value = "liikuntatunnit", layout = MainLayout.class)
@RolesAllowed({ "ADMIN", "SUPER" })
public class LiikuntatuntiListView extends VerticalLayout implements BeforeEnterObserver {

        private final LiikuntatuntiService liikuntatuntiService;
        private final Grid<Liikuntatunti> grid = new Grid<>(Liikuntatunti.class, false);

        private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("d.M.yyyy 'klo' H:mm");

        public LiikuntatuntiListView(LiikuntatuntiService liikuntatuntiService) {
                this.liikuntatuntiService = liikuntatuntiService;

                setSizeFull();
                setPadding(true);
                setSpacing(true);

                add(new H2("Liikuntatunnit"));

                configureGrid();

                Button uusi = new Button("Uusi liikuntatunti");
                uusi.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                uusi.addClickListener(e -> UI.getCurrent().navigate("liikuntatunti"));

                Button muokkaa = new Button("Muokkaa valittua");
                muokkaa.setEnabled(false);
                muokkaa.addClickListener(e -> {
                        Liikuntatunti selected = grid.asSingleSelect().getValue();
                        if (selected != null && selected.getId() != null) {
                                UI.getCurrent().navigate(
                                                "liikuntatunti/" + selected.getId());
                        }
                });

                grid.asSingleSelect()
                                .addValueChangeListener(e -> muokkaa.setEnabled(e.getValue() != null));

                grid.addItemDoubleClickListener(e -> UI.getCurrent().navigate(
                                "liikuntatunti/" + e.getItem().getId()));

                HorizontalLayout toolbar = new HorizontalLayout(uusi, muokkaa);

                add(toolbar, grid);
                refresh();
        }

        private void configureGrid() {

                grid.addColumn(Liikuntatunti::getNimi)
                                .setHeader("Nimi")
                                .setAutoWidth(true);

                grid.addColumn(Liikuntatunti::getTyyppi)
                                .setHeader("Tyyppi")
                                .setAutoWidth(true);

                grid.addColumn(tunti -> {
                        if (tunti.getAlkuaika() == null || tunti.getLoppuaika() == null) {
                                return "";
                        }

                        String alku = tunti.getAlkuaika().format(DATE_FORMAT);
                        String loppu = tunti.getLoppuaika().format(DateTimeFormatter.ofPattern("H:mm"));

                        return alku + "–" + loppu;
                })
                                .setHeader("Ajankohta")
                                .setAutoWidth(true);

                // ✅ 1:N-relaatio näkyy UI:ssa (Ohjaaja)
                grid.addColumn(tunti -> tunti.getOhjaaja() != null
                                ? tunti.getOhjaaja().getNimi()
                                : "-")
                                .setHeader("Ohjaaja")
                                .setAutoWidth(true);

                grid.addColumn(tunti -> {
                        int ilmoittautuneet = tunti.getLiikkujat() != null
                                        ? tunti.getLiikkujat().size()
                                        : 0;
                        return ilmoittautuneet + " / " + tunti.getKapasiteetti();
                })
                                .setHeader("Ilmoittautuneet")
                                .setAutoWidth(true);

                grid.setSizeFull();
        }

        private void refresh() {
                grid.setItems(liikuntatuntiService.findAllSortedByAlkuaika());
        }

        @Override
        public void beforeEnter(BeforeEnterEvent event) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication == null || authentication.getAuthorities().stream()
                                .noneMatch(authority -> authority.getAuthority().equals("ROLE_SUPER")
                                                || authority.getAuthority().equals("ROLE_ADMIN"))) {
                        event.rerouteTo(AccessDeniedView.class);
                }
        }
}