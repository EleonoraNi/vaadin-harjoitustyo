# Liikuntakeskus-sovellus (Vaadin + Spring Boot)

Tämä projekti on Savonia-ammattikorkeakoulun Java web-ohjelmointi -kurssin harjoitustyö.  
Sovellus on liikuntakeskuksen hallintajärjestelmä, jossa voidaan hallita liikkujien tietoja, jäsenyyksiä, liikuntatunteja ja ohjaajia.

## Projektin tarkoitus

Tavoitteena oli toteuttaa full stack -web-sovellus käyttäen Spring Boot -taustajärjestelmää ja Vaadin Flow -käyttöliittymää.  
Projekti keskittyy tietokantamallinnukseen, CRUD-toimintoihin, autentikointiin ja käyttöliittymäsuunnitteluun.

---


## Kuvakaappauksia

### Liikuntatuntien listaus
![Liikuntatuntien listaus](liikuntatuntilistaus.png)

### Liikuntatunnin lisäys ja muokkaus
![Lomake](Lomake.png)

### Sovelluksen yleisilme
![Yleisilme](UI-yleisilme.png)


---

## Toiminnallisuudet

- CRUD-toiminnot kaikille entiteeteille
- Autentikointi ja kirjautuminen (Spring Security)
- Roolipohjainen käyttöoikeushallinta (USER, SUPER, ADMIN)
- Rekisteröityminen
- Dynaaminen haku (Criteria API)
- CSV-datan tuonti ja vienti
- Tiedoston lataus ja tallennus
- Lokalisaatio (suomi / englanti)
- Vaadin Server Push
- Ulkoisen JavaScript-komponentin integrointi (Quill.js)

---

## Teknologiat

### Backend
- Java
- Spring Boot
- Spring Data JPA
- Spring Security

### Frontend
- Vaadin Flow

### Tietokanta
- Hibernate / JPA

### Muut
- Maven
- Docker

---

## Käynnistys

### Paikallisesti (Maven)
```bash
mvn spring-boot:run
```

## Tekijä
Eleonora Niskanen


## Dokumentaatio

Laajempi tekninen raportti (sisältää yksityiskohtaiset kuvaukset ja kaikki kuvakaappaukset):

➡️ Vaadin_harjoitustyoraportti_niskanen_eleonora.pdf
