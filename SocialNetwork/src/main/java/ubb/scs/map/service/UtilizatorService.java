package ubb.scs.map.service;

import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.domain.validators.ServiceException;
import ubb.scs.map.domain.validators.ValidationException;
import ubb.scs.map.repository.database.UtilizatorDbRepository;
import ubb.scs.map.utils.events.ChangeEventType;
import ubb.scs.map.utils.events.UtilizatorEntityChangeEvent;
import ubb.scs.map.utils.observer.Observable;
import ubb.scs.map.utils.observer.Observer;
import ubb.scs.map.utils.paging.Page;
import ubb.scs.map.utils.paging.Pageable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class UtilizatorService implements Observable<UtilizatorEntityChangeEvent> {
    private final UtilizatorDbRepository utilizatorRepository;
    private final List<Observer<UtilizatorEntityChangeEvent>> observers = new ArrayList<>();

    public UtilizatorService(UtilizatorDbRepository utilizatorRepository) {
        this.utilizatorRepository = utilizatorRepository;
    }

    public void addUtilizator(String firstName, String lastName, String username, String hashedPassword) throws ValidationException, ServiceException {
        Long largestId = StreamSupport.stream(utilizatorRepository.findAll().spliterator(), false).max(Comparator.comparing(Utilizator::getId)).map(Utilizator::getId).orElse(0L);

        Long newId = largestId + 1;
        Utilizator utilizator = new Utilizator(firstName, lastName, username, hashedPassword);
        utilizator.setId(newId);

        if (utilizatorRepository.save(utilizator).isPresent()) {
            throw new ServiceException("Utilizatorul nu a putut fi adaugat.");
        } else {
            notifyObservers(new UtilizatorEntityChangeEvent(ChangeEventType.ADD, utilizator));
        }
    }

    public void removeUtilizator(Long id) throws ServiceException {
        Optional<Utilizator> removed = utilizatorRepository.delete(id);
        if (removed.isEmpty()) {
            throw new ServiceException("Utilizatorul nu a putut fi eliminat.");
        } else {
            notifyObservers(new UtilizatorEntityChangeEvent(ChangeEventType.DELETE, removed.get()));
        }
    }

    public Utilizator updateUtilizator(Utilizator utilizator) throws ServiceException {
        if (utilizatorRepository.update(utilizator).isPresent()) {
            throw new ServiceException("Utilizatorul nu a putut fi actualizat.");
        } else {
            notifyObservers(new UtilizatorEntityChangeEvent(ChangeEventType.UPDATE, utilizator));
        }
        return utilizator;
    }


    public Iterable<Utilizator> getUtilizatori() {
        return utilizatorRepository.findAll();
    }

    public Optional<Utilizator> findByLogin(String username, String hashedPassword) {
        return utilizatorRepository.findByLogin(username, hashedPassword);
    }

    @Override
    public void addObserver(Observer<UtilizatorEntityChangeEvent> e) {
        observers.add(e);

    }

    @Override
    public void removeObserver(Observer<UtilizatorEntityChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(UtilizatorEntityChangeEvent t) {

        observers.stream().forEach(x -> x.update(t));
    }

    public Page<Utilizator> findAllOnPage(Pageable pageable) {
        return utilizatorRepository.findAllOnPage(pageable);
    }
}