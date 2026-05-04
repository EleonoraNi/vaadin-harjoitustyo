package fi.harjoitustyo.vaadin_app.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.RouterLink;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@CssImport("./themes/app/styles.css")
public class MainLayout extends AppLayout {

        public MainLayout() {
                createHeader();
                createDrawer();
                createFooter();
        }

        private void createHeader() {

                DrawerToggle drawerToggle = new DrawerToggle();

                H1 title = new H1("Liikuntakeskus");
                title.getStyle()
                                .set("font-size", "2.2rem")
                                .set("font-weight", "600")
                                .set("margin", "0")
                                .set("color", "var(--lumo-primary-color)");

                Button logoutButton = new Button("Kirjaudu ulos", e -> {
                        getUI().ifPresent(ui -> ui.getPage().setLocation("/logout"));
                });

                Span userInfo = new Span(getCurrentUserInfo());
                userInfo.getStyle()
                                .set("font-size", "var(--lumo-font-size-s)")
                                .set("color", "var(--lumo-secondary-text-color)");
                
                // ✅ YHDISTÄ KAIKKI SAMAAN RIVIIN
                com.vaadin.flow.component.orderedlayout.HorizontalLayout headerLayout = new com.vaadin.flow.component.orderedlayout.HorizontalLayout(
                                drawerToggle,
                                title,
                                userInfo,
                                logoutButton);

                headerLayout.setWidthFull();
                headerLayout.setAlignItems(
                                com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER);

                // ✅ Työnnä logout oikeaan reunaan
                headerLayout.expand(title);

                headerLayout.getStyle()
                                .set("padding", "var(--lumo-space-m)");

                addToNavbar(headerLayout);
        }

        private String getCurrentUserInfo() {
                Authentication auth = SecurityContextHolder
                                .getContext()
                                .getAuthentication();

                if (auth == null || !auth.isAuthenticated()) {
                        return "";
                }

                return "Kirjautunut: " +auth.getName();
        }

        private void createDrawer() {
                RouterLink ilmo = new RouterLink(
                                "Ilmoittautuminen", LiikkujaIlmoittautuminenView.class);
                ilmo.addComponentAsFirst(new Icon(VaadinIcon.CLIPBOARD_CHECK));
                ilmo.addClassName("nav-link");

                RouterLink ohjaajat = new RouterLink(
                                "Ohjaajat", OhjaajaListView.class);
                ohjaajat.addComponentAsFirst(new Icon(VaadinIcon.USER));
                ohjaajat.addClassName("nav-link");

                RouterLink tuntihaku = new RouterLink(
                                "Tuntihaku", LiikuntatuntiSearchView.class);
                tuntihaku.addComponentAsFirst(new Icon(VaadinIcon.SEARCH));
                tuntihaku.addClassName("nav-link");

                RouterLink liikkujat = new RouterLink(
                                "Liikkujat", LiikkujaListView.class);
                liikkujat.addComponentAsFirst(new Icon(VaadinIcon.USERS));
                liikkujat.addClassName("nav-link");

                RouterLink tunnit = new RouterLink(
                                "Liikuntatunnit", LiikuntatuntiListView.class);
                tunnit.addComponentAsFirst(new Icon(VaadinIcon.CALENDAR));
                tunnit.addClassName("nav-link");

                VerticalLayout drawerLayout = new VerticalLayout(
                                liikkujat,
                                ohjaajat,
                                tunnit,
                                tuntihaku,
                                ilmo);

                drawerLayout.setPadding(true);
                drawerLayout.setSpacing(true);

                addToDrawer(drawerLayout);
        }

        private void createFooter() {
                Footer footer = new Footer();
                footer.add(new Span("© 2026 Eleonora Niskanen"));

                footer.getStyle()
                                .set("padding", "var(--lumo-space-m)")
                                .set("font-size", "var(--lumo-font-size-s)");

                addToDrawer(footer);
        }
}