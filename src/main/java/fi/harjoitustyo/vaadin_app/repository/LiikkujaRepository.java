package fi.harjoitustyo.vaadin_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fi.harjoitustyo.vaadin_app.entity.Liikkuja;

public interface LiikkujaRepository extends JpaRepository<Liikkuja, Long> {
    Optional<Liikkuja> findByUser_Username(String username);

}
