package fi.harjoitustyo.vaadin_app.view;

import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;

import jakarta.servlet.http.HttpServletResponse;

@Route(value = "404", layout = MainLayout.class)
@PageTitle("Sivua ei löytynyt")
public class NotFoundView extends VerticalLayout implements HasErrorParameter<NotFoundException> {

    public NotFoundView() {
        add(
                new H2("404 – Sivua ei löytynyt"),
                new Paragraph("Tarkista URL-osoite tai palaa etusivulle."));
    }

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
        removeAll();
        add(
                new H2("404 – Sivua ei löytynyt"),
                new Paragraph("Tarkista URL-osoite tai palaa etusivulle."));
        return HttpServletResponse.SC_NOT_FOUND;
    }
}
