package fi.harjoitustyo.vaadin_app.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.UI;

import fi.harjoitustyo.vaadin_app.entity.Liikkuja;
import fi.harjoitustyo.vaadin_app.service.LiikkujaService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.Authentication;


@PageTitle("Liikkujat")
@Route(value = "liikkujat", layout = MainLayout.class)
@RolesAllowed({"ADMIN"})
public class LiikkujaListView extends VerticalLayout implements BeforeEnterObserver {

    private final LiikkujaService liikkujaService;
    private final Grid<Liikkuja> grid = new Grid<>(Liikkuja.class, false);
    

    public LiikkujaListView(LiikkujaService liikkujaService) {
        this.liikkujaService = liikkujaService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(new H2("Liikkujat"));

        configureGrid();

        Button uusi = new Button("Uusi liikkuja");
        uusi.addClassName("primary-action");
        uusi.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        uusi.addClickListener(e -> UI.getCurrent().navigate("liikkuja"));

        Button muokkaa = new Button("Muokkaa valittua");
        muokkaa.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        muokkaa.setEnabled(false);
        muokkaa.addClickListener(e -> {
            Liikkuja selected = grid.asSingleSelect().getValue();
            if (selected != null && selected.getId() != null) {
                UI.getCurrent().navigate("liikkuja/" + selected.getId());
            }
        });

        grid.asSingleSelect().addValueChangeListener(e -> muokkaa.setEnabled(e.getValue() != null));
        grid.addItemDoubleClickListener(e -> UI.getCurrent().navigate("liikkuja/" + e.getItem().getId()));

        
        HorizontalLayout toolbar = new HorizontalLayout(uusi, muokkaa);
        toolbar.getStyle().set("align-items", "center");

        grid.getStyle().set("border", "1px solid var(--lumo-contrast-20pct)");
        grid.getStyle().set("border-radius", "10px");

        add(toolbar, grid);


        refresh();
    }

    private void configureGrid() {
        grid.addColumn(Liikkuja::getEtunimi).setHeader("Etunimi").setAutoWidth(true);
        grid.addColumn(Liikkuja::getSukunimi).setHeader("Sukunimi").setAutoWidth(true);
        grid.addColumn(Liikkuja::getEmail).setHeader("Email").setAutoWidth(true);
        grid.addColumn(Liikkuja::getSyntymaAika).setHeader("Syntymäaika").setAutoWidth(true);
        grid.addColumn(Liikkuja::getPuhelin).setHeader("Puhelin").setAutoWidth(true);

        // Jäsenyyden voimassaolo (1:1-liikkuja-Jäsenyys)
        grid.addComponentColumn(liikkuja -> {
            if (liikkuja.getJasenyys() != null && liikkuja.getJasenyys().isVoimassa()) {
                return new Span("✅ Voimassa");
            } else {
                return new Span("❌ Ei voimassa");
            }
        }).setHeader("Jäsenyys").setAutoWidth(true);

        // Jäsenyystaso (1:1-liikkuja-Jäsenyys)
        grid.addColumn(liikkuja -> {
            if (liikkuja.getJasenyys() != null) {
                return liikkuja.getJasenyys().getTaso();
            }
            return "-";
        }).setHeader("Jäsenyystaso").setAutoWidth(true);

        grid.setSizeFull();
    }

    private void refresh() {
        grid.setItems(liikkujaService.findAll());
    }
    
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getAuthorities().stream().noneMatch(authority ->
                 authority.getAuthority().equals("ROLE_ADMIN"))) {
            event.rerouteTo(AccessDeniedView.class);
        }
    }
}
