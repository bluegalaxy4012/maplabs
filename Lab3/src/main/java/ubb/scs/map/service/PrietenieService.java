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
        if (utilizatorRepository.findOne(id1) == null || utilizatorRepository.findOne(id2) == null) {
            throw new ServiceException("Prietenia nu a putut fi adaugata.");
        }

        Prietenie prietenie = new Prietenie();
        prietenie.setId(new Tuplu<>(id1, id2));

        prietenieRepository.save(prietenie);
    }

    public void removePrietenie(Long id1, Long id2) {
        if (utilizatorRepository.findOne(id1) == null || utilizatorRepository.findOne(id2) == null) {
            throw new ServiceException("Prietenia nu a putut fi eliminata.");
        }

        Tuplu<Long, Long> tuplu1 = new Tuplu<>(id1, id2);
        Tuplu<Long, Long> tuplu2 = new Tuplu<>(id2, id1);

        if (prietenieRepository.delete(tuplu1) == null) {
            throw new ServiceException("Prietenia nu a putut fi eliminata.");
        } else {
            prietenieRepository.delete(tuplu2);
        }
    }

    public void removePrietenii(Long userId) {
        Iterable<Prietenie> prietenii = prietenieRepository.findAll();
        List<Tuplu<Long, Long>> toRemove = StreamSupport.stream(prietenii.spliterator(), false).filter(p -> p.getId().getE1().equals(userId) || p.getId().getE2().equals(userId)).map(Prietenie::getId).collect(Collectors.toList());

        for (Tuplu<Long, Long> id : toRemove) {
            prietenieRepository.delete(id);
        }
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
        List<String> ceaMaiSociabilaComunitate = new ArrayList<>();
        Long drumMaxim = 0L;

        for (Long userId : graf.keySet()) {
            if (!visited.contains(userId)) {
                List<String> comunitate = new ArrayList<>();
                Long drum = dfs_DrumMaxim(userId, graf, visited, comunitate, 0L);
                if (drum > drumMaxim) {
                    drumMaxim = drum;
                    ceaMaiSociabilaComunitate = comunitate;
                }
            }
        }
        return ceaMaiSociabilaComunitate;
    }

    private Map<Long, List<Long>> buildGraf() {
        Map<Long, List<Long>> graf = new HashMap<>();
        Iterable<Prietenie> prietenii = prietenieRepository.findAll();

        for (Prietenie prietenie : prietenii) {
            Long user1 = prietenie.getId().getE1();
            Long user2 = prietenie.getId().getE2();

            Tuplu<Long, Long> reverseTuplu = new Tuplu<>(user2, user1);
            if (prietenieRepository.findOne(reverseTuplu) != null) {
                graf.putIfAbsent(user1, new ArrayList<>());
                graf.get(user1).add(user2);

                graf.putIfAbsent(user2, new ArrayList<>());
                graf.get(user2).add(user1);
            }
        }
        return graf;
    }

    private void dfs(Long userId, Map<Long, List<Long>> graf, Set<Long> visited) {
        visited.add(userId);
        for (Long vecin : graf.get(userId)) {
            if (!visited.contains(vecin)) {
                dfs(vecin, graf, visited);
            }
        }
    }

    private Long dfs_DrumMaxim(Long userId, Map<Long, List<Long>> graf, Set<Long> visited, List<String> comunitate, Long dist) {
        visited.add(userId);
        comunitate.add(utilizatorRepository.findOne(userId).getFirstName() + " " + utilizatorRepository.findOne(userId).getLastName());
        Long maxim = dist;

        for (Long vecin : graf.get(userId)) {
            if (!visited.contains(vecin)) {
                maxim = Math.max(maxim, dfs_DrumMaxim(vecin, graf, visited, comunitate, dist + 1));
            }
        }
        return maxim;
    }
}