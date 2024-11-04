package ubb.scs.map.service;

import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Tuplu;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.domain.validators.ServiceException;
import ubb.scs.map.repository.Repository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class PrietenieService {
    private final Repository<Tuplu<Long, Long>, Prietenie> prietenieRepository;
    private final Repository<Long, Utilizator> utilizatorRepository;

    public PrietenieService(Repository<Tuplu<Long, Long>, Prietenie> prietenieRepository, Repository<Long, Utilizator> utilizatorRepository) {
        this.prietenieRepository = prietenieRepository;
        this.utilizatorRepository = utilizatorRepository;
    }

    public void addCererePrietenie(Long id1, Long id2) {
        if (utilizatorRepository.findOne(id1).isEmpty() || utilizatorRepository.findOne(id2).isEmpty()) {
            throw new ServiceException("Prietenia nu a putut fi adaugata.");
        }

        Prietenie prietenie = new Prietenie();
        prietenie.setId(new Tuplu<>(id1, id2));

        prietenieRepository.save(prietenie);
    }

    public void removePrietenie(Long id1, Long id2) {
        if (utilizatorRepository.findOne(id1).isEmpty() || utilizatorRepository.findOne(id2).isEmpty()) {
            throw new ServiceException("Prietenia nu a putut fi eliminata.");
        }

        Tuplu<Long, Long> tuplu1 = new Tuplu<>(id1, id2);
        Tuplu<Long, Long> tuplu2 = new Tuplu<>(id2, id1);

        if (prietenieRepository.delete(tuplu1).isEmpty()) {
            throw new ServiceException("Prietenia nu a putut fi eliminata.");
        } else {
            prietenieRepository.delete(tuplu2);
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
}