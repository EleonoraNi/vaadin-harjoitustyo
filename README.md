# Vaadin-harjoitustyö

Tämä projekti on Savonia-ammattikorkeakoulun Java web-ohjelmointi ‑kurssin Vaadin–Spring Boot ‑harjoitustyö. Sovellus on toteutettu Vaadin Flow ‑käyttöliittymällä ja Spring Boot ‑taustalla kerrosarkkitehtuuria noudattaen.

## Teknologiat
- Java
- Spring Boot
- Spring Data JPA
- Spring Security
- Vaadin Flow
- Hibernate / JPA
- Maven
- Docker

## Toiminnallisuudet
Sovellus sisältää neljä entiteettiä: Liikkuja, Jäsenyys, Liikuntatunti ja Ohjaaja. Entiteettien välille on toteutettu 1:1-, 1:N- ja M:N‑relaatiot, ja kaikki CRUD‑toiminnot toimivat käyttöliittymästä tietokantaan asti. Kenttien validointi on toteutettu Jakarta Validation ‑annotaatioilla.

Sovelluksessa on Spring Securityyn perustuva autentikointi ja roolipohjainen käyttöoikeuksien hallinta (USER, SUPER, ADMIN), rekisteröitymissivu sekä kustomoitu käyttöoikeusvirhenäkymä. Liikuntatunneille on toteutettu dynaaminen haku JPA Criteria APIa käyttäen.

Lisätoiminnallisuuksiin kuuluvat CSV‑datan tuonti ja vienti, tiedoston lataus ja pysyvä tallennus, lokalisointi (suomi/englanti), Vaadin Server Push, Dockerfile sekä ulkoisen JavaScript‑komponentin (Quill.js) integrointi.

## Käynnistys
Sovellus voidaan käynnistää paikallisesti Mavenilla tai ajaa Docker‑kontissa Dockerfilen avulla. Oletusportti on 8080.

## Tekijä
Eleonora Niskanen