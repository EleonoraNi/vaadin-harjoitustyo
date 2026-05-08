package fi.harjoitustyo.vaadin_app;

import fi.harjoitustyo.vaadin_app.entity.*;
import fi.harjoitustyo.vaadin_app.repository.RoleRepository;
import fi.harjoitustyo.vaadin_app.repository.UserRepository;
import fi.harjoitustyo.vaadin_app.service.LiikkujaService;
import fi.harjoitustyo.vaadin_app.service.LiikuntatuntiService;
import fi.harjoitustyo.vaadin_app.service.OhjaajaService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class TestDataInitializer {

        @Bean
        CommandLineRunner initTestData(
                        RoleRepository roleRepository,
                        UserRepository userRepository,
                        PasswordEncoder passwordEncoder,
                        OhjaajaService ohjaajaService,
                        LiikkujaService liikkujaService,
                        LiikuntatuntiService liikuntatuntiService,
                        LiikuntatuntiService tuntiService) {
                return args -> {

                        // Älä luo uudelleen jos dataa jo on
                        if (!roleRepository.findAll().isEmpty()) {
                                return;
                        }

                        if (!liikkujaService.findAll().isEmpty()) {
                                return;
                        }

                        /*
                         * =========================
                         * ROOLIT (3 kpl)
                         * =========================
                         */
                        Role adminRole = new Role("ADMIN");
                        Role superRole = new Role("SUPER");
                        Role userRole = new Role("USER");

                        adminRole = roleRepository.save(adminRole);
                        superRole = roleRepository.save(superRole);
                        userRole = roleRepository.save(userRole);

                        /*
                         * =========================
                         * KÄYTTÄJÄT (3 kpl)
                         * =========================
                         */

                        // Admin-käyttäjä
                        User adminUser = new User(
                                        "admin",
                                        passwordEncoder.encode("admin123"),
                                        "admin@liikuntakeskus.fi",
                                        "Pekka",
                                        "Hallinnoija");
                        adminUser.addRole(adminRole);
                        userRepository.save(adminUser);

                        // Super-käyttäjä
                        User superUser = new User(
                                        "super",
                                        passwordEncoder.encode("super123"),
                                        "super@liikuntakeskus.fi",
                                        "Matti",
                                        "Superkäyttäjä");
                        superUser.addRole(superRole);
                        userRepository.save(superUser);

                        // User-käyttäjä
                        User normalUser = new User(
                                        "user",
                                        passwordEncoder.encode("user123"),
                                        "user@liikuntakeskus.fi",
                                        "Kari",
                                        "Käyttäjä");
                        normalUser.addRole(userRole);
                        userRepository.save(normalUser);

                        /*
                         * =========================
                         * OHJAAJAT (5 kpl)
                         * =========================
                         */
                        Ohjaaja o1 = new Ohjaaja();
                        o1.setEtunimi("Laura");
                        o1.setSukunimi("Laine");
                        o1.setEmail("laura.laine@testi.fi");
                        o1.setErikoistuminen("Jooga");
                        o1.setPuhelin("0401111111");

                        Ohjaaja o2 = new Ohjaaja();
                        o2.setEtunimi("Janne");
                        o2.setSukunimi("Jokinen");
                        o2.setEmail("janne.jokinen@testi.fi");
                        o2.setErikoistuminen("Kehonpainotreeni");
                        o2.setPuhelin("0402222222");

                        Ohjaaja o3 = new Ohjaaja();
                        o3.setEtunimi("Sari");
                        o3.setSukunimi("Salmi");
                        o3.setEmail("sari.salmi@testi.fi");
                        o3.setErikoistuminen("Ryhmäliikunta");
                        o3.setPuhelin("0403333333");

                        ohjaajaService.save(o1);
                        ohjaajaService.save(o2);
                        ohjaajaService.save(o3);


                        /*
                         * =========================
                         * LIIKKUJAT (5 kpl)
                         * =========================
                         */
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

                        /*
                         * =========================
                         * LIIKUNTATUNNIT (8 kpl)
                         * =========================
                         */
                        Liikuntatunti t1 = new Liikuntatunti();
                        t1.setNimi("Aloittelijoiden jooga");
                        t1.setTyyppi("Jooga");
                        t1.setKapasiteetti(10);
                        t1.setAlkuaika(LocalDateTime.now().plusDays(1).withHour(18).withMinute(0));
                        t1.setLoppuaika(LocalDateTime.now().plusDays(1).withHour(19).withMinute(0));
                        t1.setOhjaaja(o1);

                        Liikuntatunti t2 = new Liikuntatunti();
                        t2.setNimi("Aamun kehonhuolto");
                        t2.setTyyppi("Huolto");
                        t2.setKapasiteetti(8);
                        t2.setAlkuaika(LocalDateTime.now().plusDays(2).withHour(9).withMinute(0));
                        t2.setLoppuaika(LocalDateTime.now().plusDays(2).withHour(10).withMinute(0));
                        t2.setOhjaaja(o3);

                        Liikuntatunti t3 = new Liikuntatunti();
                        t3.setNimi("Rentouttava jooga");
                        t3.setTyyppi("Jooga");
                        t3.setKapasiteetti(6);
                        t3.setAlkuaika(LocalDateTime.now().plusDays(3).withHour(17).withMinute(30));
                        t3.setLoppuaika(LocalDateTime.now().plusDays(3).withHour(18).withMinute(45));
                        t3.setOhjaaja(o1);

                        Liikuntatunti t4 = new Liikuntatunti();
                        t4.setNimi("Kehonpainotreeni");
                        t4.setTyyppi("Kunto");
                        t4.setKapasiteetti(3);
                        t4.setAlkuaika(LocalDateTime.now().plusDays(4).withHour(19).withMinute(0));
                        t4.setLoppuaika(LocalDateTime.now().plusDays(4).withHour(20).withMinute(0));
                        t4.setOhjaaja(o2);

                        Liikuntatunti t5 = new Liikuntatunti();
                        t5.setNimi("Ryhmäliikunta startti");
                        t5.setTyyppi("Ryhmä");
                        t5.setKapasiteetti(10);
                        t5.setAlkuaika(LocalDateTime.now().plusDays(5).withHour(18).withMinute(0));
                        t5.setLoppuaika(LocalDateTime.now().plusDays(5).withHour(19).withMinute(15));
                        t5.setOhjaaja(o3);

                        liikuntatuntiService.save(t1);
                        liikuntatuntiService.save(t2);
                        liikuntatuntiService.save(t3);
                        liikuntatuntiService.save(t4);
                        liikuntatuntiService.save(t5);
                        
                        t1.getLiikkujat().add(l1);
                        t1.getLiikkujat().add(l4);
                        t2.getLiikkujat().add(l2);
                        t3.getLiikkujat().add(l1);
                        t3.getLiikkujat().add(l5);
                        t4.getLiikkujat().add(l4);
                        t5.getLiikkujat().add(l1);
                        t5.getLiikkujat().add(l2);
                        
                        l1.getLiikuntatunnit().add(t1);
                        l4.getLiikuntatunnit().add(t1);
                        l2.getLiikuntatunnit().add(t2);
                        l1.getLiikuntatunnit().add(t3);
                        l5.getLiikuntatunnit().add(t3);
                        l4.getLiikuntatunnit().add(t4);
                        l1.getLiikuntatunnit().add(t5);
                        l2.getLiikuntatunnit().add(t5);
                        
                        tuntiService.save(t1);
                        tuntiService.save(t2);
                        tuntiService.save(t3);
                        tuntiService.save(t4);
                        tuntiService.save(t5);
                };

        }
}
