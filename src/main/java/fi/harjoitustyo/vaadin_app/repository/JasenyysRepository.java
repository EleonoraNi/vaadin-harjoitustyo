package fi.harjoitustyo.vaadin_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fi.harjoitustyo.vaadin_app.entity.Jasenyys;
import fi.harjoitustyo.vaadin_app.entity.Liikkuja;

public interface JasenyysRepository extends JpaRepository<Jasenyys, Long> {
    Optional<Jasenyys> findByLiikkuja(Liikkuja liikkuja);

}
