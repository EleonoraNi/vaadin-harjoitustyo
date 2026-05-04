package fi.harjoitustyo.vaadin_app.service;

import fi.harjoitustyo.vaadin_app.repository.LiikuntatuntiRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import fi.harjoitustyo.vaadin_app.entity.Liikuntatunti;

@Service
public class LiikuntatuntiService {

    private final LiikuntatuntiRepository repository;

    public LiikuntatuntiService(LiikuntatuntiRepository repository) {
        this.repository = repository;
    }

    public List<Liikuntatunti> findAllSortedByAlkuaika() {
        return repository.findAllByOrderByAlkuaikaAsc();
    }

    public Optional<Liikuntatunti> findById(Long id) {
        return repository.findById(id);
    }

    public List<Liikuntatunti> search(
            String teksti,
            LocalDateTime alku,
            LocalDateTime loppu,
            Long ohjaajaId) {
        return repository.search(
                teksti, alku, loppu, ohjaajaId);
    }

    @Transactional
    public Liikuntatunti save(Liikuntatunti liikuntatunti) {
        return repository.save(liikuntatunti);
    }

    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
