package fi.harjoitustyo.vaadin_app.service;

import fi.harjoitustyo.vaadin_app.entity.Liikkuja;
import fi.harjoitustyo.vaadin_app.entity.Liikuntatunti;
import fi.harjoitustyo.vaadin_app.repository.LiikkujaRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LiikkujaService {

    private final LiikkujaRepository repository;
    private final LiikuntatuntiService liikuntatuntiRepository;

    public LiikkujaService(LiikkujaRepository repository, LiikuntatuntiService liikuntatuntiRepository) {
        this.repository = repository;
        this.liikuntatuntiRepository = liikuntatuntiRepository;
    }

    public List<Liikkuja> findAll() {
        return repository.findAll();
    }

    public Optional<Liikkuja> findById(Long id) {
        return repository.findById(id);
    }
    public Optional<Liikkuja> findByUsername(String username) {
        return repository.findByUser_Username(username);
    }
    
    @Transactional
    public Liikkuja save(Liikkuja liikkuja) {
        return repository.save(liikkuja);
    }

    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public void ilmoittauduTunnille(Liikkuja liikkuja, Liikuntatunti tunti) {

        // ✅ TÄRKEÄÄ: hae managed-entiteetit
        Liikkuja managedLiikkuja = repository
                .findById(liikkuja.getId())
                .orElseThrow();

        Liikuntatunti managedTunti = liikuntatuntiRepository
                .findById(tunti.getId())
                .orElseThrow();

        if (managedTunti.getLiikkujat().contains(managedLiikkuja)) {
            throw new IllegalStateException("Liikkuja on jo ilmoittautunut");
        }

        if (managedTunti.getLiikkujat().size() >= managedTunti.getKapasiteetti()) {
            throw new IllegalStateException("Tunti on täynnä");
        }

        // ✅ MUUTA MOLEMPIA PUOLIA
        managedTunti.getLiikkujat().add(managedLiikkuja);
        managedLiikkuja.getLiikuntatunnit().add(managedTunti);

        // ✅ TALLENNA OMISTAVA PUOLI
        liikuntatuntiRepository.save(managedTunti);
    }
    


}
