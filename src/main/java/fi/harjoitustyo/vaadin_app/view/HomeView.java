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
                "Tämä on Java web-ohjelmointi kurssin Vaadin harjoitustyö.\n" +
                "Sovelluksessa voidaan lisätä, muokata, poistaa ja hakea liikuntatunteja, liikkujia, jäsenyystietoja sekä ohjaajia."       
        );
        welcome.getStyle().set("white-space", "pre-line");

        H1 features = new H1("Saatavilla olevat toiminnot:");
        Paragraph userFeatures = new Paragraph(
                "USER-rooli: Voit hakea liikuntatunteja ja ilmoittautua niihin."
        );
        Paragraph superFeatures = new Paragraph(
                "SUPER-rooli: Voit hakea, lisätä, muokata ja poistaa liikuntatunteja"
        );
        Paragraph adminFeatures = new Paragraph(
                "ADMIN-rooli: Pääset kaikkiin sovelluksen ominaisuuksiin."
        );

        add(title, welcome, features, userFeatures, superFeatures, adminFeatures);
    }
}
