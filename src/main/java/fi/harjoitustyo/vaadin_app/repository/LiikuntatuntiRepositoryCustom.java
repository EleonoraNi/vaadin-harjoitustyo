package fi.harjoitustyo.vaadin_app.repository;

import java.time.LocalDateTime;
import java.util.List;

import fi.harjoitustyo.vaadin_app.entity.Liikuntatunti;

public interface LiikuntatuntiRepositoryCustom {

    List<Liikuntatunti> search(
        String teksti,
        LocalDateTime alku,
        LocalDateTime loppu,
        String ohjaajaTeksti
    );
}
