package fi.harjoitustyo.vaadin_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fi.harjoitustyo.vaadin_app.entity.Liikuntatunti;

public interface LiikuntatuntiRepository extends
        JpaRepository<Liikuntatunti, Long>,
        LiikuntatuntiRepositoryCustom {
    List<Liikuntatunti> findAllByOrderByAlkuaikaAsc();

}
