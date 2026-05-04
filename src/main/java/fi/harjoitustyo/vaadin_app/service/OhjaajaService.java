package fi.harjoitustyo.vaadin_app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import fi.harjoitustyo.vaadin_app.entity.Ohjaaja;
import fi.harjoitustyo.vaadin_app.repository.OhjaajaRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OhjaajaService {
    private final OhjaajaRepository ohjaajaRepository;

    public OhjaajaService(OhjaajaRepository ohjaajaRepository) {
        this.ohjaajaRepository = ohjaajaRepository;
    }
    
    public List<Ohjaaja> findAll() {
        return ohjaajaRepository.findAll();
    }   
    
    public Optional<Ohjaaja> findById(Long id) {
        return ohjaajaRepository.findById(id);
    }
    
    @Transactional
    public Ohjaaja save(Ohjaaja ohjaaja) {
        return ohjaajaRepository.save(ohjaaja);
    }
    
    @Transactional
    public void deleteById(Long id) {
        ohjaajaRepository.deleteById(id);
    }
}
