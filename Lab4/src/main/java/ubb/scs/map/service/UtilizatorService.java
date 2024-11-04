package ubb.scs.map.service;

import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.domain.validators.ServiceException;
import ubb.scs.map.domain.validators.ValidationException;
import ubb.scs.map.repository.Repository;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class UtilizatorService {
    private final Repository<Long, Utilizator> utilizatorRepository;

    public UtilizatorService(Repository<Long, Utilizator> utilizatorRepository) {
        this.utilizatorRepository = utilizatorRepository;
    }

    public void addUtilizator(String firstName, String lastName) throws ValidationException, ServiceException {
        Long largestId = StreamSupport.stream(utilizatorRepository.findAll().spliterator(), false).max(Comparator.comparing(Utilizator::getId)).map(Utilizator::getId).orElse(0L);

        Long newId = largestId + 1;
        Utilizator utilizator = new Utilizator(firstName, lastName);
        utilizator.setId(newId);

        if (utilizatorRepository.save(utilizator).isPresent()) {
            throw new ServiceException("Utilizatorul nu a putut fi adaugat.");
        }
    }

    public void removeUtilizator(Long id) throws ServiceException {
        Optional<Utilizator> removed = utilizatorRepository.delete(id);
        if (removed.isEmpty()) {
            throw new ServiceException("Utilizatorul nu a putut fi eliminat.");
        }
    }

    public Iterable<Utilizator> getUtilizatori() {
        return utilizatorRepository.findAll();
    }
}