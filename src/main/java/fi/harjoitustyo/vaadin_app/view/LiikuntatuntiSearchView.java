package fi.harjoitustyo.vaadin_app.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;


import fi.harjoitustyo.vaadin_app.entity.Liikuntatunti;
import fi.harjoitustyo.vaadin_app.entity.Ohjaaja;
import fi.harjoitustyo.vaadin_app.service.LiikuntatuntiService;
import fi.harjoitustyo.vaadin_app.service.OhjaajaService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

@CssImport("./styles/liikuntatunti-search-view.css")
@PageTitle("Tuntihaku")
@Route(value = "", layout = MainLayout.class)
public class LiikuntatuntiSearchView extends VerticalLayout
                implements BeforeEnterObserver {

        private final LiikuntatuntiService liikuntatuntiService;
        private final OhjaajaService ohjaajaService;

        private final Grid<Liikuntatunti> grid = new Grid<>(Liikuntatunti.class, false);

        public LiikuntatuntiSearchView(
                        LiikuntatuntiService liikuntatuntiService,
                        OhjaajaService ohjaajaService) {
                this.liikuntatuntiService = liikuntatuntiService;
                this.ohjaajaService = ohjaajaService;

                setSizeFull();
                setSpacing(true);
                setPadding(true);

                ResourceBundle msg = messages();

                H2 title = new H2(msg.getString("title.search"));
                title.addClassName("search-title");

                add(
                                buildLanguageSwitcher(),
                                title,
                                buildSearchForm(),
                                buildGrid());
        }

        /*
         * -------------------------------------------------
         * Locale asetetaan ENNEN näkymän rakentamista
         * -------------------------------------------------
         */
        @Override
        public void beforeEnter(BeforeEnterEvent event) {

                event.getLocation()
                                .getQueryParameters()
                                .getParameters()
                                .getOrDefault("lang", List.of())
                                .stream()
                                .findFirst()
                                .ifPresent(lang -> {
                                        if ("en".equalsIgnoreCase(lang)) {
                                                UI.getCurrent().setLocale(Locale.ENGLISH);
                                        } else if ("fi".equalsIgnoreCase(lang)) {
                                                UI.getCurrent().setLocale(new Locale("fi"));
                                        }
                                });

                // 🔴 TÄRKEÄ: tyhjennä ja rakenna uudelleen OIKEALLA localella
                removeAll();

                ResourceBundle msg = messages();
                H2 title = new H2(msg.getString("title.search"));
                title.addClassName("search-title");
                
                add(
                                buildLanguageSwitcher(),
                                title,
                                buildSearchForm(),
                                buildGrid());
        }

        /*
         * -------------------------------------------------
         * Kieliresurssit UI:n localen mukaan
         * -------------------------------------------------
         */
        private ResourceBundle messages() {
                Locale locale = UI.getCurrent() != null && UI.getCurrent().getLocale() != null
                                ? UI.getCurrent().getLocale()
                                : Locale.getDefault();
                return ResourceBundle.getBundle("messages", locale);
        }

        /*
         * -------------------------------------------------
         * Kielenvaihtopainikkeet
         * -------------------------------------------------
         */
        private HorizontalLayout buildLanguageSwitcher() {

                Button fi = new Button("FI", e -> UI.getCurrent().navigate("",
                                new QueryParameters(
                                                Map.of("lang", List.of("fi")))));

                Button en = new Button("EN", e -> UI.getCurrent().navigate("",
                                new QueryParameters(
                                                Map.of("lang", List.of("en")))));

                HorizontalLayout layout = new HorizontalLayout(fi, en);
                layout.setWidthFull();
                layout.setJustifyContentMode(JustifyContentMode.END);
                return layout;
        }

        /*
         * -------------------------------------------------
         * Hakukentät
         * -------------------------------------------------
         */
        private HorizontalLayout buildSearchForm() {
                ResourceBundle msg = messages();

                TextField teksti = new TextField(msg.getString("field.text"));
                teksti.setPlaceholder(
                                msg.getString("placeholder.text"));

                DatePicker alkuPaiva = new DatePicker(msg.getString("field.startDate"));

                DatePicker loppuPaiva = new DatePicker(msg.getString("field.endDate"));

                ComboBox<Ohjaaja> ohjaaja = new ComboBox<>(msg.getString("field.instructor"));
                ohjaaja.setItems(ohjaajaService.findAll());
                ohjaaja.setItemLabelGenerator(Ohjaaja::getNimi);
                ohjaaja.setClearButtonVisible(true);

                Button hae = new Button(msg.getString("button.search"));

                hae.addClickListener(e -> {
                        LocalDateTime alku = alkuPaiva.getValue() != null
                                        ? alkuPaiva.getValue().atStartOfDay()
                                        : null;

                        LocalDateTime loppu = loppuPaiva.getValue() != null
                                        ? loppuPaiva.getValue().atTime(23, 59)
                                        : null;

                        Long ohjaajaId = ohjaaja.getValue() != null
                                        ? ohjaaja.getValue().getId()
                                        : null;

                        grid.setItems(
                                        liikuntatuntiService.search(
                                                        teksti.getValue(),
                                                        alku,
                                                        loppu,
                                                        ohjaajaId));
                });

                HorizontalLayout layout = new HorizontalLayout(
                                teksti, alkuPaiva, loppuPaiva, ohjaaja, hae);
                layout.setAlignItems(Alignment.END);
                layout.setWidthFull();
                layout.addClassName("search-container");
                return layout;
        }

        /*
         * -------------------------------------------------
         * Tuloslista
         * -------------------------------------------------
         */

        private Grid<Liikuntatunti> buildGrid() {
                final ResourceBundle msg = messages();

                // 🔑 TÄRKEÄ: poista vanhat sarakkeet
                grid.removeAllColumns();
                grid.addClassName("search-grid");

                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy 'klo' H:mm");
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");

                grid.addColumn(Liikuntatunti::getNimi)
                                .setHeader(msg.getString("grid.name"));

                grid.addColumn(Liikuntatunti::getTyyppi)
                                .setHeader(msg.getString("grid.type"));

                grid.addColumn(t -> {
                        if (t.getAlkuaika() == null || t.getLoppuaika() == null) {
                                return "";
                        }
                        return t.getAlkuaika().format(dateFormatter)
                                        + " – "
                                        + t.getLoppuaika().format(timeFormatter);
                }).setHeader(msg.getString("grid.time"));

                grid.addColumn(t -> t.getOhjaaja() != null
                                ? t.getOhjaaja().getNimi()
                                : "").setHeader(msg.getString("grid.instructor"));

                grid.addColumn(Liikuntatunti::getKapasiteetti)
                                .setHeader(msg.getString("grid.capacity"));

                grid.setSizeFull();
                return grid;
        }

}