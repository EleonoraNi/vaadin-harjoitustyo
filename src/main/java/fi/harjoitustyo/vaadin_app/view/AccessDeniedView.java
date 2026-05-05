package fi.harjoitustyo.vaadin_app.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;

@Route(value = "access-denied", layout = MainLayout.class)
@PageTitle("Ei käyttöoikeutta")
public class AccessDeniedView extends VerticalLayout {

    public AccessDeniedView() {
        add(
                new H2("403 – Ei käyttöoikeutta"),
                new Paragraph("Sinulla ei ole oikeuksia tähän näkymään."));
    }
}
