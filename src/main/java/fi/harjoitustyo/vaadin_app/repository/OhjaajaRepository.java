package fi.harjoitustyo.vaadin_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fi.harjoitustyo.vaadin_app.entity.Ohjaaja;

public interface OhjaajaRepository extends JpaRepository<Ohjaaja, Long> {

}
