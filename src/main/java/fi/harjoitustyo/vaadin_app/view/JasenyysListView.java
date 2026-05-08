package fi.harjoitustyo.vaadin_app.view;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import fi.harjoitustyo.vaadin_app.entity.Jasenyys;
import fi.harjoitustyo.vaadin_app.service.JasenyysService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@PageTitle("Jäsenyydet")
@Route(value = "jasenyydet", layout = MainLayout.class)
@RolesAllowed({ "ADMIN", "SUPER", "USER" })
public class JasenyysListView extends VerticalLayout implements BeforeEnterObserver {

    private final JasenyysService jasenyysService;
    private final Grid<Jasenyys> grid = new Grid<>(Jasenyys.class, false);

    public JasenyysListView(JasenyysService jasenyysService) {
        this.jasenyysService = jasenyysService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(new H2("Jäsenyydet"));

        configureGrid();
        updateList();
    }

    private void configureGrid() {
        grid.addColumn(j -> j.getLiikkuja().getEtunimi() + " " + j.getLiikkuja().getSukunimi())
                .setHeader("Liikkuja")
                .setSortable(true);

        grid.addColumn(Jasenyys::getAlkamisPaiva)
                .setHeader("Alkamis päivä")
                .setSortable(true);

        grid.addColumn(Jasenyys::getPaattymisPaiva)
                .setHeader("Päättymis päivä")
                .setSortable(true);

        grid.addColumn(Jasenyys::getTaso)
                .setHeader("Taso")
                .setSortable(true);

        grid.addComponentColumn(jasenyys -> {
            if (jasenyys.isVoimassa()) {
                return new Span("✅ Voimassa");
            } else {
                return new Span("❌ Ei voimassa");
            }
        }).setHeader("Voimassaolo").setSortable(true);

        grid.addColumn(Jasenyys::getTyyppi)
                .setHeader("Tyyppi")
                .setSortable(true);

        grid.addColumn(Jasenyys::getKaupunki)
                .setHeader("Kaupunki")
                .setSortable(true);

        add(grid);
    }

    private void updateList() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdminOrSuper = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_SUPER"));

        if (isAdminOrSuper) {
            grid.setItems(jasenyysService.findAll());
        } else {
            String username = auth.getName();

            grid.setItems(
                    jasenyysService.findForCurrentUser(username)
                            .stream()
                            .toList());
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getAuthorities().stream()
                .noneMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN") ||
                        authority.getAuthority().equals("ROLE_SUPER") ||
                        authority.getAuthority().equals("ROLE_USER"))) {
            event.rerouteTo(AccessDeniedView.class);
        }
    }
}