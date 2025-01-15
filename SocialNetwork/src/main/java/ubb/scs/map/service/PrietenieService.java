package ubb.scs.map.service;

import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Tuplu;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.domain.validators.ServiceException;
import ubb.scs.map.repository.database.PrietenieDbRepository;
import ubb.scs.map.repository.database.UtilizatorDbRepository;
import ubb.scs.map.utils.Constants;
import ubb.scs.map.utils.events.ChangeEventType;
import ubb.scs.map.utils.events.PrietenieEntityChangeEvent;
import ubb.scs.map.utils.observer.Observable;
import ubb.scs.map.utils.observer.Observer;
import ubb.scs.map.utils.paging.Page;
import ubb.scs.map.utils.paging.Pageable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class PrietenieService implements Observable<PrietenieEntityChangeEvent> {
    private final PrietenieDbRepository prietenieRepository;
    private final UtilizatorDbRepository utilizatorRepository;
    private final List<Observer<PrietenieEntityChangeEvent>> observers = new ArrayList<>();

    public PrietenieService(PrietenieDbRepository prietenieRepository) {
        this.prietenieRepository = prietenieRepository;
        this.utilizatorRepository = prietenieRepository.getUtilizatorRepository();
    }

    public void addCererePrietenie(Long id1, Long id2) {
        if (utilizatorRepository.findOne(id1).isEmpty() || utilizatorRepository.findOne(id2).isEmpty()) {
            throw new ServiceException("Prietenia nu a putut fi adaugata.");
        }

        Prietenie prietenie = new Prietenie();
        prietenie.setId(new Tuplu<>(id1, id2));

        prietenieRepository.save(prietenie);
        notifyObservers(new PrietenieEntityChangeEvent(ChangeEventType.ADD, prietenie));
    }

    public void removePrietenie(Long id1, Long id2) {
        if (utilizatorRepository.findOne(id1).isEmpty() || utilizatorRepository.findOne(id2).isEmpty()) {
            throw new ServiceException("Prietenia nu a putut fi eliminata.");
        }

        Prietenie p1 = new Prietenie(), p2 = new Prietenie();
        Tuplu<Long, Long> tuplu1 = new Tuplu<>(id1, id2);
        Tuplu<Long, Long> tuplu2 = new Tuplu<>(id2, id1);
        p1.setId(tuplu1);
        p2.setId(tuplu2);

        if (prietenieRepository.delete(tuplu1).isEmpty()) {
            throw new ServiceException("Prietenia nu a putut fi eliminata.");
        } else {
            prietenieRepository.delete(tuplu2);
            notifyObservers(new PrietenieEntityChangeEvent(ChangeEventType.DELETE, p1));
            //poate if not empty deleteu inca una
        }
    }

    public void removePrietenii(Long userId) {
        Iterable<Prietenie> prietenii = prietenieRepository.findAll();

        List<Tuplu<Long, Long>> toRemove = StreamSupport.stream(prietenii.spliterator(), false).filter(p -> p.getId().getE1().equals(userId) || p.getId().getE2().equals(userId)).map(Prietenie::getId).collect(Collectors.toList());

        toRemove.forEach(prietenieRepository::delete);
    }

    public Iterable<Prietenie> getPrietenii() {
        return prietenieRepository.findAll();
    }

    public String getStatus(Long id1, Long id2) {
        Tuplu<Long, Long> tupludus = new Tuplu<>(id1, id2);
        Tuplu<Long, Long> tupluintors = new Tuplu<>(id2, id1);

        if (prietenieRepository.findOne(tupludus).isPresent() && prietenieRepository.findOne(tupluintors).isPresent()) {
            if (prietenieRepository.findOne(tupludus).get().getRequestFrom().isAfter(prietenieRepository.findOne(tupluintors).get().getRequestFrom())) {
                return "Prietenie acceptata de la data de " + prietenieRepository.findOne(tupludus).get().getRequestFrom().format(Constants.DATE_TIME_FORMATTER);
            } else {
                return "Prietenie acceptata de la data de " + prietenieRepository.findOne(tupluintors).get().getRequestFrom().format(Constants.DATE_TIME_FORMATTER);
            }
        } else if (prietenieRepository.findOne(tupludus).isPresent()) {
            return "Cerere trimisa la data de " + prietenieRepository.findOne(tupludus).get().getRequestFrom().format(Constants.DATE_TIME_FORMATTER);
        } else if (prietenieRepository.findOne(tupluintors).isPresent()) {
            return "Cerere primita la data de " + prietenieRepository.findOne(tupluintors).get().getRequestFrom().format(Constants.DATE_TIME_FORMATTER);
        } else {
            return "Nu exista nicio cerere";
        }

    }

    public int getNumarPrieteni(Long userId) {
        return prietenieRepository.countFriends(userId);
    }

    private Map<Long, List<Long>> buildGraf() {
        Map<Long, List<Long>> graf = new HashMap<>();
        Iterable<Prietenie> prietenii = prietenieRepository.findAll();

        for (Prietenie prietenie : prietenii) {
            Long userId1 = prietenie.getId().getE1();
            Long userId2 = prietenie.getId().getE2();

            Tuplu<Long, Long> reverseTuplu = new Tuplu<>(userId2, userId1);
            if (prietenieRepository.findOne(reverseTuplu).isPresent()) {
                graf.putIfAbsent(userId1, new ArrayList<>());
                graf.get(userId1).add(userId2);

                graf.putIfAbsent(userId2, new ArrayList<>());
                graf.get(userId2).add(userId1);
            }
        }
        return graf;
    }

    public Long getNumarComunitati() {
        Map<Long, List<Long>> graf = buildGraf();
        Set<Long> visited = new HashSet<>();
        Long numarComunitati = 0L;

        for (Long userId : graf.keySet()) {
            if (!visited.contains(userId)) {
                numarComunitati++;
                dfs(userId, graf, visited);
            }
        }
        return numarComunitati;
    }

    public List<String> getCeaMaiSociabilaComunitate() {
        Map<Long, List<Long>> graf = buildGraf();
        Set<Long> visited = new HashSet<>();
        List<String> celMaiLungDrum = new ArrayList<>();

        for (Long userId : graf.keySet()) {
            if (!visited.contains(userId)) {
                List<String> drumComponenta = new ArrayList<>();
                dfsDrumMaxim(userId, graf, visited, new ArrayList<>(), drumComponenta);
                if (drumComponenta.size() > celMaiLungDrum.size()) {
                    celMaiLungDrum = drumComponenta;
                }
            }
        }
        return celMaiLungDrum;
    }

    private void dfs(Long userId, Map<Long, List<Long>> graf, Set<Long> visited) {
        visited.add(userId);
        for (Long vecin : graf.get(userId)) {
            if (!visited.contains(vecin)) {
                dfs(vecin, graf, visited);
            }
        }
    }

    private void dfsDrumMaxim(Long userId, Map<Long, List<Long>> graf, Set<Long> visited, List<String> drumCurent, List<String> drumMaxim) {
        visited.add(userId);
        drumCurent.add(utilizatorRepository.findOne(userId).map(u -> u.getFirstName() + " " + u.getLastName()).orElseThrow(() -> new ServiceException("Un utilizator nu a putut fi gasit.")));

        if (drumCurent.size() > drumMaxim.size()) {
            drumMaxim.clear();
            drumMaxim.addAll(drumCurent);
        }

        for (Long vecin : graf.get(userId)) {
            if (!visited.contains(vecin)) {
                dfsDrumMaxim(vecin, graf, visited, drumCurent, drumMaxim);
            }
        }
        drumCurent.remove(drumCurent.size() - 1);
        visited.remove(userId);
    }

    @Override
    public void addObserver(Observer<PrietenieEntityChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<PrietenieEntityChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(PrietenieEntityChangeEvent t) {
        observers.forEach(x -> x.update(t));
    }


    public Page<Utilizator> findAllOnPage(Pageable pageable, Long userId) {
        return prietenieRepository.findAllOnPage(pageable, userId);
    }
}