package fi.harjoitustyo.vaadin_app.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;

import jakarta.servlet.http.HttpServletResponse;

@Route(value = "error", layout = MainLayout.class)
@PageTitle("Virhe")
public class InternalErrorView extends VerticalLayout implements HasErrorParameter<Exception> {

    public InternalErrorView() {
        add(
                new H2("500 – Sisäinen palvelinvirhe"),
                new Paragraph("Tapahtui odottamaton virhe. Yritä myöhemmin uudelleen."));
    }

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<Exception> parameter) {
        removeAll();
        add(
                new H2("500 – Sisäinen palvelinvirhe"),
                new Paragraph("Tapahtui odottamaton virhe. Yritä myöhemmin uudelleen."));
        return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
    }
}