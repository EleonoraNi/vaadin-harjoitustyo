package fi.harjoitustyo.vaadin_app.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.RouterLink;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Collection;
import com.vaadin.flow.theme.lumo.Lumo;

@StyleSheet(Lumo.STYLESHEET)
@StyleSheet("styles.css")
public class MainLayout extends AppLayout {

        private VerticalLayout mainContent;
        private Footer footer;

        public MainLayout() {
                createHeader();
                createDrawer();

                mainContent = new VerticalLayout();
                mainContent.setSizeFull();
                mainContent.setPadding(true);
                mainContent.setSpacing(true);

                footer = createFooter();

                VerticalLayout wrapper = new VerticalLayout();
                wrapper.setSizeFull();
                wrapper.setPadding(false);
                wrapper.setSpacing(false);

                wrapper.add(mainContent, footer);
                wrapper.expand(mainContent); // ✅ työntää footerin alas

                setContent(wrapper);

        }

        private void createHeader() {

                DrawerToggle drawerToggle = new DrawerToggle();

                H1 title = new H1("Liikuntakeskus");
                title.getStyle()
                                .set("font-size", "2.2rem")
                                .set("font-weight", "600")
                                .set("margin", "0")
                                .set("color", "var(--lumo-primary-color)");

                Button authButton;
                Button registerButton = null;

                if (isUserLoggedIn()) {
                        authButton = new Button("Kirjaudu ulos", e -> {
                                getUI().ifPresent(ui -> ui.getPage().setLocation("/logout"));
                        });
                } else {
                        registerButton = new Button("Rekisteröidy", e -> {
                                getUI().ifPresent(ui -> ui.navigate("register"));
                        });

                        authButton = new Button("Kirjaudu", e -> {
                                getUI().ifPresent(ui -> ui.getPage().setLocation("/login"));
                        });
                }

                Span userInfo = new Span(getCurrentUserInfo());
                userInfo.getStyle()
                                .set("font-size", "var(--lumo-font-size-s)")
                                .set("color", "var(--lumo-secondary-text-color)");

                // ✅ YHDISTÄ KAIKKI SAMAAN RIVIIN

                HorizontalLayout headerLayout;

                if (registerButton != null) {
                        headerLayout = new HorizontalLayout(
                                        drawerToggle,
                                        title,
                                        userInfo,
                                        registerButton,
                                        authButton);
                } else {
                        headerLayout = new HorizontalLayout(
                                        drawerToggle,
                                        title,
                                        userInfo,
                                        authButton);
                }

                headerLayout.setWidthFull();
                headerLayout.setAlignItems(
                                com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER);

                // ✅ Työnnä logout oikeaan reunaan
                headerLayout.expand(title);

                headerLayout.getStyle()
                                .set("padding", "var(--lumo-space-m)");

                addToNavbar(headerLayout);
        }

        private boolean isUserLoggedIn() {
                Authentication auth = SecurityContextHolder
                                .getContext()
                                .getAuthentication();

                return auth != null
                                && auth.isAuthenticated()
                                && !(auth.getPrincipal() instanceof String
                                                && auth.getPrincipal().equals("anonymousUser"));
        }

        private String getCurrentUserInfo() {
                Authentication auth = SecurityContextHolder
                                .getContext()
                                .getAuthentication();

                if (auth == null || !auth.isAuthenticated()) {
                        return "";
                }

                return "Kirjautunut: " + auth.getName();
        }

        private boolean hasRole(String role) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth == null) {
                        return false;
                }
                Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
                return authorities.stream()
                                .anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
        }

        private void createDrawer() {
                VerticalLayout drawerLayout = new VerticalLayout();
                drawerLayout.setPadding(true);
                drawerLayout.setSpacing(true);

                // HOME - näkee kaikki
                RouterLink home = new RouterLink("Etusivu", HomeView.class);
                home.addComponentAsFirst(new Icon(VaadinIcon.HOME));
                home.addClassName("nav-link");
                drawerLayout.add(home);

                // TUNTIHAKU - USER, SUPER, ADMIN
                if (hasRole("USER") || hasRole("SUPER") || hasRole("ADMIN")) {
                        RouterLink tuntihaku = new RouterLink("Tuntihaku", LiikuntatuntiSearchView.class);
                        tuntihaku.addComponentAsFirst(new Icon(VaadinIcon.SEARCH));
                        tuntihaku.addClassName("nav-link");
                        drawerLayout.add(tuntihaku);
                }

                // ILMOITTAUTUMINEN - USER, SUPER, ADMIN
                if (hasRole("USER") || hasRole("ADMIN")) {
                        RouterLink ilmo = new RouterLink("Ilmoittautuminen", LiikkujaIlmoittautuminenView.class);
                        ilmo.addComponentAsFirst(new Icon(VaadinIcon.CLIPBOARD_CHECK));
                        ilmo.addClassName("nav-link");
                        drawerLayout.add(ilmo);
                }

                // LIIKUNTATUNNIT - SUPER, ADMIN
                if (hasRole("SUPER") || hasRole("ADMIN")) {
                        RouterLink tunnit = new RouterLink("Liikuntatunnit", LiikuntatuntiListView.class);
                        tunnit.addComponentAsFirst(new Icon(VaadinIcon.CALENDAR));
                        tunnit.addClassName("nav-link");
                        drawerLayout.add(tunnit);
                }

                // ADMIN näkymät
                if (hasRole("ADMIN")) {
                        RouterLink liikkujat = new RouterLink("Liikkujat", LiikkujaListView.class);
                        liikkujat.addComponentAsFirst(new Icon(VaadinIcon.USERS));
                        liikkujat.addClassName("nav-link");
                        drawerLayout.add(liikkujat);

                        RouterLink ohjaajat = new RouterLink("Ohjaajat", OhjaajaListView.class);
                        ohjaajat.addComponentAsFirst(new Icon(VaadinIcon.USER));
                        ohjaajat.addClassName("nav-link");
                        drawerLayout.add(ohjaajat);

                        RouterLink jasenyydet = new RouterLink("Jäsenyydet", JasenyysListView.class);
                        jasenyydet.addComponentAsFirst(new Icon(VaadinIcon.USER_CHECK));
                        jasenyydet.addClassName("nav-link");
                        drawerLayout.add(jasenyydet);

                        RouterLink quill = new RouterLink("Quill Editor", QuillEditorView.class);
                        quill.addComponentAsFirst(new Icon(VaadinIcon.EDIT));
                        quill.addClassName("nav-link");
                        drawerLayout.add(quill);
                }

                addToDrawer(drawerLayout);
        }

        private Footer createFooter() {
                Footer footer = new Footer();
                footer.setWidthFull();

                HorizontalLayout layout = new HorizontalLayout(
                                new Span("© 2026 Eleonora Niskanen"),
                                new Span("Java web-ohjelmointi kurssin harjoitustyö"),
                                new Anchor("https://www.savonia.fi", "Savonia AMK"));

                layout.setWidthFull();
                layout.setSpacing(true);
                layout.setPadding(true);
                layout.setJustifyContentMode(
                                JustifyContentMode.BETWEEN);

                footer.add(layout);
                footer.addClassName("app-footer");

                return footer;
        }

        @Override
        public void showRouterLayoutContent(HasElement content) {
                mainContent.removeAll();

                if (content instanceof Component) {
                        mainContent.add((Component) content);
                }
        }

}