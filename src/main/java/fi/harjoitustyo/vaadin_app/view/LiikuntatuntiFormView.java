package fi.harjoitustyo.vaadin_app.view;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.*;

import fi.harjoitustyo.vaadin_app.entity.Liikuntatunti;
import fi.harjoitustyo.vaadin_app.entity.Ohjaaja;
import fi.harjoitustyo.vaadin_app.service.LiikuntatuntiService;
import fi.harjoitustyo.vaadin_app.service.OhjaajaService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.Authentication;

@PageTitle("Liikuntatunti")
@Route(value = "liikuntatunti", layout = MainLayout.class)
@RolesAllowed({"ADMIN", "SUPER"})
public class LiikuntatuntiFormView extends VerticalLayout
        implements HasUrlParameter<Long>, BeforeEnterObserver {

    private final LiikuntatuntiService liikuntatuntiService;
    private final OhjaajaService ohjaajaService;

    private final BeanValidationBinder<Liikuntatunti> binder = new BeanValidationBinder<>(Liikuntatunti.class);

    private Liikuntatunti current;

    // Kentät
    private final TextField nimi = new TextField("Nimi");
    private final TextField tyyppi = new TextField("Tyyppi");
    private final DatePicker paiva = new DatePicker("Päivä");
    private final TimePicker alkuaika = new TimePicker("Alkuaika");
    private final TimePicker loppuaika = new TimePicker("Loppuaika");
    private final IntegerField kapasiteetti = new IntegerField("Kapasiteetti");

    // 1:N Ohjaaja
    private final ComboBox<Ohjaaja> ohjaaja = new ComboBox<>("Ohjaaja");

    private final Button tallenna = new Button("Tallenna");
    private final Button poista = new Button("Poista");
    private final Button peruuta = new Button("Peruuta");

    public LiikuntatuntiFormView(LiikuntatuntiService liikuntatuntiService,
            OhjaajaService ohjaajaService) {
        this.liikuntatuntiService = liikuntatuntiService;
        this.ohjaajaService = ohjaajaService;

        setWidthFull();
        setPadding(true);
        setSpacing(true);

        add(new H2("Liikuntatunnin tiedot"));

        configureForm();
        add(buildFormLayout(), buildButtons());
    }

    @Override
    public void setParameter(BeforeEvent event,
            @OptionalParameter Long id) {

        if (id == null) {
            current = new Liikuntatunti(); // uusi
        } else {
            current = liikuntatuntiService.findById(id).orElse(null);
            if (current == null) {
                Notification.show("Liikuntatuntia ei löytynyt (id=" + id + ")");
                UI.getCurrent().navigate("liikuntatunnit");
                return;
            }
        }

        binder.readBean(current);
        poista.setEnabled(current.getId() != null);
    }

    private void configureForm() {

        binder.removeBinding(alkuaika);
        binder.removeBinding(loppuaika);

        // Kapasiteetin rajat UI:ssa (vastaa @Min/@Max)
        kapasiteetti.setMin(1);
        kapasiteetti.setMax(100);
        kapasiteetti.setStep(1);

        // Ohjaaja ComboBox (1:N-relaatio UI:ssa)
        ohjaaja.setItems(ohjaajaService.findAll());
        ohjaaja.setItemLabelGenerator(Ohjaaja::getNimi);
        ohjaaja.setClearButtonVisible(true);
        ohjaaja.setPlaceholder("Valitse ohjaaja");

        binder.forField(nimi)
                .bind(Liikuntatunti::getNimi, Liikuntatunti::setNimi);

        binder.forField(tyyppi)
                .bind(Liikuntatunti::getTyyppi, Liikuntatunti::setTyyppi);

        binder.forField(kapasiteetti)
                .bind(Liikuntatunti::getKapasiteetti, Liikuntatunti::setKapasiteetti);

        // Ohjaaja sidotaan käsin
        binder.forField(ohjaaja)
                .bind(Liikuntatunti::getOhjaaja,
                        Liikuntatunti::setOhjaaja);

        binder.forField(alkuaika)
                .withValidator(Objects::nonNull, "Valitse alkuaika")
                .bind(
                        tunti -> tunti.getAlkuaika() != null
                                ? tunti.getAlkuaika().toLocalTime()
                                : null,
                        (tunti, time) -> {
                            if (paiva.getValue() != null && time != null) {
                                tunti.setAlkuaika(
                                        LocalDateTime.of(paiva.getValue(), time));
                            }
                        });

        binder.forField(loppuaika)
                .withValidator(Objects::nonNull, "Valitse loppuaika")
                .bind(
                        tunti -> tunti.getLoppuaika() != null
                                ? tunti.getLoppuaika().toLocalTime()
                                : null,
                        (tunti, time) -> {
                            if (paiva.getValue() != null && time != null) {
                                tunti.setLoppuaika(
                                        LocalDateTime.of(paiva.getValue(), time));
                            }
                        });

        binder.withValidator(tunti -> {
            if (tunti.getAlkuaika() == null || tunti.getLoppuaika() == null) {
                return true;
            }
            return tunti.getLoppuaika().isAfter(tunti.getAlkuaika());
        }, "Loppuajan täytyy olla alkamisajan jälkeen");

        tallenna.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        poista.addThemeVariants(ButtonVariant.LUMO_ERROR);

        tallenna.addClickListener(e -> save());
        poista.addClickListener(e -> delete());
        peruuta.addClickListener(e -> UI.getCurrent().navigate("liikuntatunnit"));

        poista.setEnabled(false);
    }

    private FormLayout buildFormLayout() {
        FormLayout form = new FormLayout();

        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("700px", 2));

        form.add(
                nimi, tyyppi, paiva,
                alkuaika, loppuaika,
                kapasiteetti, ohjaaja);

        form.setMaxWidth("900px");
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
            liikuntatuntiService.save(current);
            Notification.show("Tallennettu");
            UI.getCurrent().navigate("liikuntatunnit");
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
            liikuntatuntiService.deleteById(current.getId());
            Notification.show("Poistettu");
            UI.getCurrent().navigate("liikuntatunnit");
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
        if (authentication == null || authentication.getAuthorities().stream().noneMatch(authority ->
                authority.getAuthority().equals("ROLE_SUPER") || authority.getAuthority().equals("ROLE_ADMIN"))) {
            event.rerouteTo(AccessDeniedView.class);
        }
    }
}