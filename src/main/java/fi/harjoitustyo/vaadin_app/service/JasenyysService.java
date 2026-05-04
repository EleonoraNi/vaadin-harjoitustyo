package fi.harjoitustyo.vaadin_app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import fi.harjoitustyo.vaadin_app.repository.JasenyysRepository;
import fi.harjoitustyo.vaadin_app.entity.Jasenyys;

import org.springframework.transaction.annotation.Transactional;

@Service
public class JasenyysService {
    
    private final JasenyysRepository repository;
    
    public JasenyysService(JasenyysRepository repository) {
        this.repository = repository;
    }
    
    public List<Jasenyys> findAll() {
        return repository.findAll();
    }
    
    public Optional<Jasenyys> findById(Long id) {
        return repository.findById(id);
    }
    
    @Transactional
    public Jasenyys save(Jasenyys jasenyys) {
        return repository.save(jasenyys);
    }
    
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
