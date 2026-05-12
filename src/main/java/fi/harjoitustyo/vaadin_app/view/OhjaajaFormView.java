package fi.harjoitustyo.vaadin_app.view;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.*;

import com.vaadin.flow.server.StreamResource;

import fi.harjoitustyo.vaadin_app.entity.Ohjaaja;
import fi.harjoitustyo.vaadin_app.service.OhjaajaService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.Authentication;

@PageTitle("Ohjaaja")
@Route(value = "ohjaaja", layout = MainLayout.class)
@RolesAllowed({ "ADMIN" })
public class OhjaajaFormView extends VerticalLayout
        implements HasUrlParameter<Long>, BeforeEnterObserver {

    private final OhjaajaService ohjaajaService;
    private final BeanValidationBinder<Ohjaaja> binder = new BeanValidationBinder<>(Ohjaaja.class);

    private Ohjaaja current;

    // Kentät
    private final TextField etunimi= new TextField("Etunimi");
    private final TextField sukunimi = new TextField("Sukunimi");
    private final EmailField email = new EmailField("Email");
    private final TextField erikoistuminen = new TextField("Erikoistuminen");
    private final TextField puhelin = new TextField("Puhelin");

    private Upload upload;
    private Anchor ladattuTiedostoLinkki;
    private Span ladattuTiedostoTeksti;

    private final Button tallenna = new Button("Tallenna");
    private final Button poista = new Button("Poista");
    private final Button peruuta = new Button("Peruuta");

    public OhjaajaFormView(OhjaajaService ohjaajaService) {
        this.ohjaajaService = ohjaajaService;

        setWidthFull();
        setPadding(true);
        setSpacing(true);

        add(new H2("Ohjaajan tiedot"));

        // --- Upload ---
        upload = new Upload();
        upload.setAcceptedFileTypes(".pdf", ".png", ".jpg");
        upload.setMaxFiles(1);

        upload.setReceiver((filename, mimeType) -> {
            try {
                Path target = Paths
                        .get(System.getProperty("user.dir"))
                        .resolve("uploads/ohjaajat")
                        .resolve(filename);

                Files.createDirectories(target.getParent());

                if (current != null) {
                    current.setTiedostoPolku(target.toString());
                }

                return Files.newOutputStream(
                        target,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException("Tiedoston tallennus epäonnistui", e);
            }
        });

        upload.addSucceededListener(e -> Notification.show("Tiedosto ladattu"));

        // --- Ladatun tiedoston näyttö ---
        ladattuTiedostoTeksti = new Span();
        ladattuTiedostoLinkki = new Anchor();
        ladattuTiedostoLinkki.getElement().setAttribute("download", true);
        ladattuTiedostoLinkki.setVisible(false);

        configureForm();

        FormLayout form = buildFormLayout();
        form.add(upload);

        add(form, ladattuTiedostoTeksti, ladattuTiedostoLinkki, buildButtons());
    }

    @Override
    public void setParameter(BeforeEvent event,
            @OptionalParameter Long id) {

        if (id == null) {
            current = new Ohjaaja();
        } else {
            current = ohjaajaService.findById(id).orElse(null);
            if (current == null) {
                Notification.show("Ohjaajaa ei löytynyt (id=" + id + ")");
                UI.getCurrent().navigate("ohjaajat");
                return;
            }
        }

        binder.readBean(current);
        poista.setEnabled(current.getId() != null);

        // Näytä ladattu tiedosto, jos on
        if (current.getTiedostoPolku() != null) {
            Path filePath = Paths.get(current.getTiedostoPolku());

            StreamResource resource = new StreamResource(
                    filePath.getFileName().toString(),
                    () -> {
                        try {
                            return Files.newInputStream(filePath);
                        } catch (IOException e) {
                            Notification.show("Tiedostoa ei löytynyt");
                            return InputStream.nullInputStream();
                        }
                    });

            try {
                resource.setContentType(
                        Files.probeContentType(filePath));
            } catch (IOException ignored) {
            }

            ladattuTiedostoTeksti.setText("Ohjaajalle ladattu tiedosto:");
            ladattuTiedostoLinkki.setText(filePath.getFileName().toString());
            ladattuTiedostoLinkki.setHref(resource);
            ladattuTiedostoLinkki.setVisible(true);

        } else {
            ladattuTiedostoTeksti.setText("Ei ladattua tiedostoa");
            ladattuTiedostoLinkki.setVisible(false);
        }
    }

    private void configureForm() {
        binder.bindInstanceFields(this);

        email.setClearButtonVisible(true);
        email.setErrorMessage("Anna kelvollinen sähköposti");

        tallenna.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        poista.addThemeVariants(ButtonVariant.LUMO_ERROR);

        tallenna.addClickListener(e -> save());
        poista.addClickListener(e -> delete());
        peruuta.addClickListener(
                e -> UI.getCurrent().navigate(OhjaajaListView.class));

        poista.setEnabled(false);
    }

    private FormLayout buildFormLayout() {
        FormLayout form = new FormLayout();
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("600px", 2));
        form.add(etunimi,sukunimi, email, erikoistuminen, puhelin);
        form.setMaxWidth("800px");
        return form;
    }

    private HorizontalLayout buildButtons() {
        HorizontalLayout buttons = new HorizontalLayout(tallenna, poista, peruuta);
        buttons.setSpacing(true);
        return buttons;
    }

    private void save() {
        try {
            binder.writeBean(current);
            ohjaajaService.save(current);
            Notification.show("Tallennettu");
            UI.getCurrent().navigate(OhjaajaListView.class);
        } catch (ValidationException ex) {
            Notification.show(
                    "Tarkista kentät – validointi esti tallennuksen.");
        } catch (Exception ex) {
            Notification.show(
                    "Tallennus epäonnistui: " + ex.getMessage(),
                    5000,
                    Notification.Position.MIDDLE);
        }
    }

    private void delete() {
        if (current == null || current.getId() == null) {
            Notification.show("Ei poistettavaa – tallenna ensin.");
            return;
        }
        try {
            ohjaajaService.deleteById(current.getId());
            Notification.show("Poistettu");
            UI.getCurrent().navigate("ohjaajat");
        } catch (Exception ex) {
            Notification.show(
                    "Poisto epäonnistui: " + ex.getMessage(),
                    5000,
                    Notification.Position.MIDDLE);
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getAuthorities().stream()
                .noneMatch(authority -> 
                         authority.getAuthority().equals("ROLE_ADMIN"))) {
            event.rerouteTo(AccessDeniedView.class);
        }
    }
}