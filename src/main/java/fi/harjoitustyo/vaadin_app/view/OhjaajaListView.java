package fi.harjoitustyo.vaadin_app.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fi.harjoitustyo.vaadin_app.entity.Ohjaaja;
import fi.harjoitustyo.vaadin_app.service.OhjaajaService;
import jakarta.annotation.security.RolesAllowed;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility.Background;
import com.vaadin.flow.theme.lumo.LumoUtility.BorderRadius;
import com.vaadin.flow.theme.lumo.LumoUtility.BoxShadow;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.TextColor;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@PageTitle("Ohjaajat")
@Route(value = "ohjaajat", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class OhjaajaListView extends VerticalLayout {

    private final OhjaajaService ohjaajaService;
    private final Grid<Ohjaaja> grid = new Grid<>(Ohjaaja.class, false);

    private StreamResource createCsvExport() {
        StringBuilder csv = new StringBuilder();
        csv.append("nimi,email,erikoistuminen,puhelin\n");

        ohjaajaService.findAll().forEach(o -> {
            csv.append(o.getNimi()).append(",")
                    .append(o.getEmail()).append(",")
                    .append(o.getErikoistuminen()).append(",")
                    .append(o.getPuhelin() != null ? o.getPuhelin() : "")
                    .append("\n");
        });

        return new StreamResource(
                "ohjaajat.csv",
                () -> new ByteArrayInputStream(
                        csv.toString().getBytes(StandardCharsets.UTF_8)));
    }

    public OhjaajaListView(OhjaajaService ohjaajaService) {
        this.ohjaajaService = ohjaajaService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        addClassNames(Padding.LARGE, Background.BASE);

        H2 otsikko = new H2("Ohjaajat");
        otsikko.addClassNames(
                TextColor.PRIMARY,
                Margin.Bottom.MEDIUM);
        add(otsikko);

        configureGrid();

        Button uusi = new Button("Uusi ohjaaja");
        uusi.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        uusi.addClickListener(e -> UI.getCurrent().navigate("ohjaaja"));

        Button muokkaa = new Button("Muokkaa valittua");
        muokkaa.setEnabled(false);
        muokkaa.addClickListener(e -> {
            Ohjaaja selected = grid.asSingleSelect().getValue();
            if (selected != null && selected.getId() != null) {
                UI.getCurrent().navigate("ohjaaja/" + selected.getId());
            }
        });

        grid.asSingleSelect()
                .addValueChangeListener(e -> muokkaa.setEnabled(e.getValue() != null));

        grid.addItemDoubleClickListener(e -> UI.getCurrent().navigate("ohjaaja/" + e.getItem().getId()));

        Anchor vieCsv = new Anchor(createCsvExport(), "Vie CSV");
        vieCsv.getElement().setAttribute("download", true);

        MemoryBuffer csvBuffer = new MemoryBuffer();
        Upload tuoCsv = new Upload(csvBuffer);
        tuoCsv.setAcceptedFileTypes(".csv");
        tuoCsv.setMaxFiles(1);

        tuoCsv.addSucceededListener(e -> {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(csvBuffer.getInputStream(), StandardCharsets.UTF_8))) {

                reader.readLine(); // ohitetaan otsikkorivi

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");

                    if (parts.length < 3)
                        continue;

                    Ohjaaja o = new Ohjaaja();
                    o.setNimi(parts[0].trim());
                    o.setEmail(parts[1].trim());
                    o.setErikoistuminen(parts[2].trim());
                    if (parts.length > 3) {
                        o.setPuhelin(parts[3].trim());
                    }

                    ohjaajaService.save(o);
                }

                Notification.show("CSV tuotu onnistuneesti");
                refresh();

            } catch (Exception ex) {
                Notification.show("CSV tuonti epäonnistui");
            }
        });

        HorizontalLayout toolbar = new HorizontalLayout(uusi, muokkaa,tuoCsv ,vieCsv);

        toolbar.addClassNames(Margin.Bottom.MEDIUM, Padding.SMALL, BorderRadius.MEDIUM, BoxShadow.SMALL);

        add(toolbar, grid);
        refresh();
    }

    private void configureGrid() {
        grid.addColumn(Ohjaaja::getNimi)
                .setHeader("Nimi")
                .setAutoWidth(true);

        grid.addColumn(Ohjaaja::getErikoistuminen)
                .setHeader("Erikoistuminen")
                .setAutoWidth(true);

        grid.addColumn(Ohjaaja::getEmail)
                .setHeader("Email")
                .setAutoWidth(true);

        grid.addColumn(Ohjaaja::getPuhelin)
                .setHeader("Puhelin")
                .setAutoWidth(true);

        grid.setSizeFull();
    }

    private void refresh() {
        grid.setItems(ohjaajaService.findAll());
    }
}
