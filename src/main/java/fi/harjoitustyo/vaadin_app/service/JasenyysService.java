package fi.harjoitustyo.vaadin_app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import fi.harjoitustyo.vaadin_app.repository.JasenyysRepository;
import fi.harjoitustyo.vaadin_app.repository.UserRepository;
import fi.harjoitustyo.vaadin_app.entity.Jasenyys;
import fi.harjoitustyo.vaadin_app.entity.User;

import org.springframework.transaction.annotation.Transactional;

@Service
public class JasenyysService {

    private final JasenyysRepository repository;
    private final UserRepository userRepository;

    public JasenyysService(JasenyysRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public List<Jasenyys> findAll() {
        return repository.findAll();
    }

    public Optional<Jasenyys> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<Jasenyys> findForCurrentUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent() && user.get().getLiikkuja() != null) {
            return repository.findByLiikkuja(user.get().getLiikkuja());
        }

        return Optional.empty();
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
