package fi.harjoitustyo.vaadin_app.view;

import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;

import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;

import com.vaadin.flow.router.*;

import fi.harjoitustyo.vaadin_app.entity.Jasenyys;
import fi.harjoitustyo.vaadin_app.entity.Liikkuja;
import fi.harjoitustyo.vaadin_app.service.LiikkujaService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.Authentication;

@PageTitle("Liikkuja")
@Route(value = "liikkuja", layout = MainLayout.class)
@RolesAllowed({ "ADMIN", "SUPER", "USER" })
public class LiikkujaFormView extends VerticalLayout implements HasUrlParameter<Long>, BeforeEnterObserver {

    private final LiikkujaService liikkujaService;

    private final BeanValidationBinder<Liikkuja> binder = new BeanValidationBinder<>(Liikkuja.class);

    private final BeanValidationBinder<Jasenyys> jasenyysBinder = new BeanValidationBinder<>(Jasenyys.class);

    private Liikkuja current;

    // Kentät – nimien pitää täsmätä Liikkuja-propertyihin
    // binder.bindInstanceFields(this) -metodissa
    private final TextField etunimi = new TextField("Etunimi");
    private final TextField sukunimi = new TextField("Sukunimi");
    private final EmailField email = new EmailField("Email");
    private final DatePicker syntymaAika = new DatePicker("Syntymäaika");
    private final TextField puhelin = new TextField("Puhelin");

    // Jäsenyys-kentät
    private final DatePicker jasenyysAlku = new DatePicker("Jäsenyyden alkamispäivä");
    private final DatePicker jasenyysLoppu = new DatePicker("Jäsenyyden päättymispäivä");
    private final TextField jasenyysTaso = new TextField("Jäsenyystaso (1–3)");
    private final TextField jasenyysTyyppi = new TextField("Jäsenyyden tyyppi");
    private final TextField jasenyysKaupunki = new TextField("Kaupunki");

    private final Button tallenna = new Button("Tallenna");
    private final Button poista = new Button("Poista");
    private final Button peruuta = new Button("Peruuta");

    public LiikkujaFormView(LiikkujaService liikkujaService) {
        this.liikkujaService = liikkujaService;

        setWidthFull();
        setPadding(true);
        setSpacing(true);

        add(new H2("Liikkujan tiedot"));

        configureForm();
        add(buildFormLayout(), buildButtons());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdminOrSuper = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_SUPER"));

        if (!isAdminOrSuper) {
            poista.setVisible(false);
        }

    }

    @Override
    public void setParameter(BeforeEvent event, @com.vaadin.flow.router.OptionalParameter Long id) {
        if (id == null) {
            current = new Liikkuja(); // uusi
        } else {
            current = liikkujaService.findById(id).orElse(null);
            if (current == null) {
                Notification.show("Liikkujaa ei löytynyt (id=" + id + ")");
                UI.getCurrent().navigate("liikkujat");
                return;
            }
        }

        binder.readBean(current);

        if (current.getJasenyys() != null) {
            jasenyysBinder.readBean(current.getJasenyys());
        } else {
            jasenyysBinder.readBean(new Jasenyys());
        }

        poista.setEnabled(current.getId() != null);
    }

    private void configureForm() {
        binder.bindInstanceFields(this);

        jasenyysBinder.bind(jasenyysAlku, "alkamisPaiva");
        jasenyysBinder.bind(jasenyysLoppu, "paattymisPaiva");

        jasenyysBinder.forField(jasenyysTaso)
                .withConverter(
                        value -> value == null || value.isBlank() ? null : Integer.valueOf(value),
                        value -> value == null ? "" : value.toString(),
                        "Anna numero 1–3")
                .bind("taso");

        jasenyysBinder.bind(jasenyysTyyppi, "tyyppi");
        jasenyysBinder.bind(jasenyysKaupunki, "kaupunki");

        email.setClearButtonVisible(true);

        tallenna.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        poista.addThemeVariants(ButtonVariant.LUMO_ERROR);

        tallenna.addClickListener(e -> save());
        poista.addClickListener(e -> delete());
        peruuta.addClickListener(e -> UI.getCurrent().navigate(LiikkujaListView.class));

        poista.setEnabled(false);
    }

    private FormLayout buildFormLayout() {
        FormLayout form = new FormLayout();
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("600px", 2));

        form.add(etunimi, sukunimi, email, syntymaAika,
                puhelin, jasenyysAlku, jasenyysLoppu, jasenyysTaso, jasenyysTyyppi, jasenyysKaupunki);
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
            binder.writeBean(current); // tämä täyttää vain Liikkuja

            if (jasenyysTaso.getValue() != null && !jasenyysTaso.getValue().isBlank()) {

                Jasenyys j = current.getJasenyys();
                if (j == null) {
                    j = new Jasenyys();
                    j.setLiikkuja(current);
                }

                jasenyysBinder.writeBean(j); // ✅ TÄMÄ laukoo UI-validoinnin

                current.setJasenyys(j);

            } else {
                // ✅ sallii pelkän liikkujan tallennuksen
                current.setJasenyys(null);
            }

            liikkujaService.save(current);

            Notification.show("Tallennettu");
            UI.getCurrent().navigate(LiikkujaListView.class);

        } catch (ValidationException ex) {
            Notification.show("Tarkista kentät");
        }
    }

    private void delete() {
        if (current == null || current.getId() == null) {
            Notification.show("Ei poistettavaa – tallenna ensin.");
            return;
        }

        try {
            liikkujaService.deleteById(current.getId());
            Notification.show("Poistettu");
            UI.getCurrent().navigate("liikkujat");
        } catch (Exception ex) {
            Notification.show("Poisto epäonnistui: " + ex.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getAuthorities().stream()
                .noneMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN")
                        || authority.getAuthority().equals("ROLE_SUPER")
                        || authority.getAuthority().equals("ROLE_USER"))) {
            event.rerouteTo(AccessDeniedView.class);
        }
    }

}