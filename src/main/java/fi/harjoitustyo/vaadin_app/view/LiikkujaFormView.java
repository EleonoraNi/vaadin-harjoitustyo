package fi.harjoitustyo.vaadin_app.view;

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

@PageTitle("Liikkuja")
@Route(value = "liikkuja")
public class LiikkujaFormView extends VerticalLayout implements HasUrlParameter<Long> {

    private final LiikkujaService liikkujaService;

    private final BeanValidationBinder<Liikkuja> binder = new BeanValidationBinder<>(Liikkuja.class);

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
        poista.setEnabled(current.getId() != null);
    }

    private void configureForm() {
        binder.bindInstanceFields(this);

        email.setClearButtonVisible(true);
        email.setErrorMessage("Syötä kelvollinen sähköposti");

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
                puhelin, jasenyysAlku, jasenyysLoppu, jasenyysTaso);
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
            // 🔹 Luodaan jäsenyys vain jos käyttäjä on syöttänyt tason
            if (current.getJasenyys() == null &&
                    jasenyysTaso.getValue() != null &&
                    !jasenyysTaso.getValue().isBlank()) {

                Jasenyys j = new Jasenyys();
                j.setLiikkuja(current);
                current.setJasenyys(j);
            }

            binder.writeBean(current); // validoinnit tapahtuvat tässä
            liikkujaService.save(current);

            Notification.show("Tallennettu");
            UI.getCurrent().navigate(LiikkujaListView.class);

        } catch (ValidationException ex) {
            Notification.show("Tarkista kentät – validointi esti tallennuksen.");
        } catch (Exception ex) {
            Notification.show("Tallennus epäonnistui: " + ex.getMessage(),
                    5000, Notification.Position.MIDDLE);
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
}