package fi.harjoitustyo.vaadin_app.repository;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import fi.harjoitustyo.vaadin_app.entity.Liikuntatunti;


@Repository
public class LiikuntatuntiRepositoryImpl
        implements LiikuntatuntiRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Liikuntatunti> search(
            String teksti,
            LocalDateTime alku,
            LocalDateTime loppu,
            Long ohjaajaId,
            String ohjaajaTeksti) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Liikuntatunti> cq = cb.createQuery(Liikuntatunti.class);

        Root<Liikuntatunti> tunti = cq.from(Liikuntatunti.class);
        List<Predicate> predicates = new ArrayList<>();

        // (nimi LIKE OR tyyppi LIKE)
        if (teksti != null && !teksti.isBlank()) {
            String like = "%" + teksti.toLowerCase() + "%";

            Predicate nimiLike = cb.like(cb.lower(tunti.get("nimi")), like);
            Predicate tyyppiLike = cb.like(cb.lower(tunti.get("tyyppi")), like);

            predicates.add(cb.or(nimiLike, tyyppiLike));
        }

        // Päivämääräväli
        if (alku != null) {
            predicates.add(
                    cb.greaterThanOrEqualTo(
                            tunti.get("alkuaika"), alku));
        }

        if (loppu != null) {
            predicates.add(
                    cb.lessThanOrEqualTo(
                            tunti.get("loppuaika"), loppu));
        }

        Join<Liikuntatunti, Object> ohjaajaJoin = null;
        if (ohjaajaId != null || (ohjaajaTeksti != null && !ohjaajaTeksti.isBlank())) {
            ohjaajaJoin = tunti.join("ohjaaja");
        }

        if (ohjaajaId != null) {
            predicates.add(
                    cb.equal(
                            ohjaajaJoin.get("id"),
                            ohjaajaId));
        }

        if (ohjaajaTeksti != null && !ohjaajaTeksti.isBlank()) {
            String likeOhjaaja = "%" + ohjaajaTeksti.toLowerCase() + "%";
            Predicate nimiLike = cb.like(cb.lower(ohjaajaJoin.get("nimi")), likeOhjaaja);
            Predicate erikoistuminenLike = cb.like(cb.lower(ohjaajaJoin.get("erikoistuminen")), likeOhjaaja);
            predicates.add(cb.or(nimiLike, erikoistuminenLike));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.asc(tunti.get("alkuaika")));

        return em.createQuery(cq).getResultList();
    }
}
