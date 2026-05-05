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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Collection;

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

        private void createFooter() {
                Footer footer = new Footer();
                footer.add(new Span("© 2026 Eleonora Niskanen"));

                footer.getStyle()
                                .set("padding", "var(--lumo-space-m)")
                                .set("font-size", "var(--lumo-font-size-s)");

                addToDrawer(footer);
        }
}