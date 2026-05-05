package fi.harjoitustyo.vaadin_app.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fi.harjoitustyo.vaadin_app.entity.Role;
import fi.harjoitustyo.vaadin_app.entity.User;
import fi.harjoitustyo.vaadin_app.repository.RoleRepository;
import fi.harjoitustyo.vaadin_app.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import fi.harjoitustyo.vaadin_app.entity.Liikkuja;
import java.time.LocalDate;

@Route("register")
@PageTitle("Rekisteröidy")
public class RegisterView extends VerticalLayout {

    public RegisterView(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {

        setWidth("400px");
        setAlignItems(Alignment.CENTER);
        setSpacing(true);

        H2 title = new H2("Rekisteröityminen");

        TextField username = new TextField("Käyttäjätunnus");
        TextField etunimi = new TextField("Etunimi");
        TextField sukunimi = new TextField("Sukunimi");
        TextField email = new TextField("Sähköposti");
        PasswordField password = new PasswordField("Salasana");
        PasswordField confirmPassword = new PasswordField("Salasana uudelleen");

        Button registerButton = new Button("Rekisteröidy");

        registerButton.addClickListener(e -> {

            if (etunimi.isEmpty() || sukunimi.isEmpty() || email.isEmpty()) {
                Notification.show("Täytä kaikki tiedot");
                return;
            }

            if (username.isEmpty() || password.isEmpty()) {
                Notification.show("Täytä kaikki kentät");
                return;
            }

            if (!password.getValue().equals(confirmPassword.getValue())) {
                Notification.show("Salasanat eivät täsmää");
                return;
            }

            if (userRepository.findByUsername(username.getValue()).isPresent()) {
                Notification.show("Käyttäjätunnus on jo käytössä");
                return;
            }

            Role userRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new RuntimeException("ROLE USER puuttuu"));

            // 1️⃣ Luo Liikkuja
            Liikkuja liikkuja = new Liikkuja();
            liikkuja.setEtunimi(etunimi.getValue());
            liikkuja.setSukunimi(sukunimi.getValue());
            liikkuja.setEmail(email.getValue());

            // Pakolliset kentät – anna järkevät oletukset
            liikkuja.setSyntymaAika(LocalDate.of(2000, 1, 1));
            liikkuja.setPuhelin("");

            // 2️⃣ Luo User
            User user = new User();
            user.setUsername(username.getValue());
            user.setPassword(passwordEncoder.encode(password.getValue()));
            user.setEtunimi(etunimi.getValue());
            user.setSukunimi(sukunimi.getValue());
            user.setEmail(email.getValue());
            user.getRoles().add(userRole);

            // 3️⃣ Linkitä User ↔ Liikkuja
            user.setLiikkuja(liikkuja);
            liikkuja.setUser(user);

            // 4️⃣ Tallenna (cascade hoitaa Liikkujan)
            userRepository.save(user);

            Notification.show("Kiitos rekisteröitymisestä, voit nyt kirjautua sisään.");
            getUI().ifPresent(ui -> ui.navigate(HomeView.class));
        });

        add(title, username, etunimi, sukunimi, email, password, confirmPassword, registerButton);
    }
}