package fi.harjoitustyo.vaadin_app.view;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Etusivu")
public class HomeView extends VerticalLayout {

    public HomeView() {
        setPadding(true);
        setSpacing(true);

        H1 title = new H1("Tervetuloa Liikuntakeskus-sovellukseen!");
        Paragraph welcome = new Paragraph(
                "Tämä on Vaadin + Spring Boot -pohjainen harjoitustyö. " +
                "Sovelluksessa voit hallita liikuntakeskuksen aktiviteeteja, ohjaajia ja jäsenyyksiä."
        );

        H1 features = new H1("Saatavilla olevat toiminnot:");
        Paragraph userFeatures = new Paragraph(
                "USER-rooli: Voit osallistua tuntihaun avulla liikuntatunteihin ja ilmoittautua."
        );
        Paragraph superFeatures = new Paragraph(
                "SUPER-rooli: Lisäksi voit hallita liikuntatunteja ja katso niiden tietoja."
        );
        Paragraph adminFeatures = new Paragraph(
                "ADMIN-rooli: Pääset kaikkiin sovelluksen ominaisuuksiin."
        );

        add(title, welcome, features, userFeatures, superFeatures, adminFeatures);
    }
}
