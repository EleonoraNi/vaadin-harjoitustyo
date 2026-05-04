package fi.harjoitustyo.vaadin_app;

import fi.harjoitustyo.vaadin_app.entity.*;
import fi.harjoitustyo.vaadin_app.service.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.boot.CommandLineRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class TestDataInitializer {

    @Bean
    CommandLineRunner initTestData(
            LiikkujaService liikkujaService,
            OhjaajaService ohjaajaService,
            LiikuntatuntiService liikuntatuntiService
    ) {
        return args -> {

            // Älä luo uudelleen jos dataa jo on
            if (!liikkujaService.findAll().isEmpty()) {
                return;
            }

            /* =========================
               OHJAAJAT (3 kpl)
               ========================= */
            Ohjaaja o1 = new Ohjaaja();
            o1.setNimi("Laura Laine");
            o1.setEmail("laura.laine@testi.fi");
            o1.setErikoistuminen("Jooga");
            o1.setPuhelin("0401111111");

            Ohjaaja o2 = new Ohjaaja();
            o2.setNimi("Janne Jokinen");
            o2.setEmail("janne.jokinen@testi.fi");
            o2.setErikoistuminen("Kehonpainotreeni");
            o2.setPuhelin("0402222222");

            Ohjaaja o3 = new Ohjaaja();
            o3.setNimi("Sari Salmi");
            o3.setEmail("sari.salmi@testi.fi");
            o3.setErikoistuminen("Ryhmäliikunta");
            o3.setPuhelin("0403333333");

            ohjaajaService.save(o1);
            ohjaajaService.save(o2);
            ohjaajaService.save(o3);

            List<Ohjaaja> ohjaajat = List.of(o1, o2, o3);

            /* =========================
               LIIKKUJAT (5 kpl)
               ========================= */
            Liikkuja l1 = new Liikkuja();
            l1.setEtunimi("Anna");
            l1.setSukunimi("Virtanen");
            l1.setEmail("anna@testi.fi");
            l1.setPuhelin("0401234567");
            l1.setSyntymaAika(LocalDate.of(1995, 5, 10));

            Jasenyys j1 = new Jasenyys();
            j1.setAlkamisPaiva(LocalDate.now().minusMonths(2));
            j1.setPaattymisPaiva(LocalDate.now().plusMonths(6));
            j1.setTaso(2);
            j1.setLiikkuja(l1);
            l1.setJasenyys(j1);

            Liikkuja l2 = new Liikkuja();
            l2.setEtunimi("Mikko");
            l2.setSukunimi("Korhonen");
            l2.setEmail("mikko@testi.fi");
            l2.setPuhelin("0509876543");
            l2.setSyntymaAika(LocalDate.of(1988, 11, 22));

            Jasenyys j2 = new Jasenyys();
            j2.setAlkamisPaiva(LocalDate.now().minusYears(1));
            j2.setPaattymisPaiva(LocalDate.now().minusDays(10));
            j2.setTaso(1);
            j2.setLiikkuja(l2);
            l2.setJasenyys(j2);

            Liikkuja l3 = new Liikkuja();
            l3.setEtunimi("Ella");
            l3.setSukunimi("Mäkinen");
            l3.setEmail("ella@testi.fi");
            l3.setPuhelin("0445556666");
            l3.setSyntymaAika(LocalDate.of(2000, 3, 15));

            Liikkuja l4 = new Liikkuja();
            l4.setEtunimi("Antti");
            l4.setSukunimi("Heikkinen");
            l4.setEmail("antti@testi.fi");
            l4.setPuhelin("0457778888");
            l4.setSyntymaAika(LocalDate.of(1992, 7, 1));

            Jasenyys j4 = new Jasenyys();
            j4.setAlkamisPaiva(LocalDate.now());
            j4.setPaattymisPaiva(LocalDate.now().plusMonths(12));
            j4.setTaso(3);
            j4.setLiikkuja(l4);
            l4.setJasenyys(j4);

            Liikkuja l5 = new Liikkuja();
            l5.setEtunimi("Sara");
            l5.setSukunimi("Nieminen");
            l5.setEmail("sara@testi.fi");
            l5.setPuhelin("0469990000");
            l5.setSyntymaAika(LocalDate.of(1998, 9, 30));

            liikkujaService.save(l1);
            liikkujaService.save(l2);
            liikkujaService.save(l3);
            liikkujaService.save(l4);
            liikkujaService.save(l5);

            /* =========================
               LIIKUNTATUNNIT (10 kpl)
               ========================= */
            for (int i = 1; i <= 10; i++) {
                Liikuntatunti t = new Liikuntatunti();
                t.setNimi("Liikuntatunti " + i);
                t.setTyyppi(i % 2 == 0 ? "Kunto" : "Jooga");
                t.setKapasiteetti(20 + i);
                t.setAlkuaika(LocalDateTime.now().plusDays(i).withHour(18).withMinute(0));
                t.setLoppuaika(LocalDateTime.now().plusDays(i).withHour(19).withMinute(30));
                t.setOhjaaja(ohjaajat.get(i % ohjaajat.size()));

                liikuntatuntiService.save(t);
            }
        };
    }
}