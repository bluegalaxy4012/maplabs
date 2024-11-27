package ubb.scs.map.service;

import ubb.scs.map.domain.Mesaj;
import ubb.scs.map.repository.Repository;
import ubb.scs.map.utils.events.ChangeEventType;
import ubb.scs.map.utils.events.MesajEntityChangeEvent;
import ubb.scs.map.utils.observer.Observable;
import ubb.scs.map.utils.observer.Observer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MesajService implements Observable<MesajEntityChangeEvent> {
    private final Repository<Long, Mesaj> mesajRepository;
    private final List<Observer<MesajEntityChangeEvent>> observers = new ArrayList<>();

    public MesajService(Repository<Long, Mesaj> mesajRepository) {
        this.mesajRepository = mesajRepository;
    }

    public List<Mesaj> getMessages(Long userId) {
        return StreamSupport.stream(mesajRepository.findAll().spliterator(), false).filter(mesaj -> mesaj.getFrom().getId().equals(userId) || mesaj.getTo().stream().anyMatch(utilizator -> utilizator.getId().equals(userId))).sorted(Comparator.comparing(Mesaj::getDate)).collect(Collectors.toList());
    }

    public void sendMessage(Mesaj mesaj) {
        mesajRepository.save(mesaj);
        notifyObservers(new MesajEntityChangeEvent(ChangeEventType.ADD, mesaj));
    }

    @Override
    public void addObserver(Observer<MesajEntityChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<MesajEntityChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(MesajEntityChangeEvent t) {
        observers.forEach(observer -> observer.update(t));
    }
}