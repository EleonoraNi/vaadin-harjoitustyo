package fi.harjoitustyo.vaadin_app;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.Theme;

@Push
@Theme(value = "app")
public class ApplicationShell implements AppShellConfigurator {
}

