package fi.harjoitustyo.vaadin_app.view;

import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.Authentication;

@Route(value = "quill", layout = MainLayout.class)
@JavaScript("https://cdn.quilljs.com/1.3.7/quill.js")
@RolesAllowed({ "ADMIN" })
public class QuillEditorView extends VerticalLayout implements BeforeEnterObserver {

    public QuillEditorView() {
        setSizeFull();

        Div editor = new Div();
        editor.setId("editor");
        editor.setWidth("600px");
        editor.setHeight("300px");

        add(editor);

        // Käynnistetään Quill JavaScriptillä
        getElement().executeJs(
                "var quill = new Quill('#editor', { theme: 'snow' });");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getAuthorities().stream()
                .noneMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))) {
            event.rerouteTo(AccessDeniedView.class);
        }
    }

}