package ubb.scs.map;

import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Tuplu;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.domain.validators.PrietenieValidator;
import ubb.scs.map.domain.validators.UtilizatorValidator;
import ubb.scs.map.domain.validators.Validator;
import ubb.scs.map.repository.Repository;
import ubb.scs.map.repository.file.PrietenieRepository;
import ubb.scs.map.repository.file.UtilizatorRepository;
import ubb.scs.map.service.PrietenieService;
import ubb.scs.map.service.UtilizatorService;
import ubb.scs.map.ui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
/*
        Repository<Long, Utilizator> repo = new InMemoryRepository<Long, Utilizator>(new UtilizatorValidator());
        Repository<Long, Utilizator> repoFile = new UtilizatorRepository(new UtilizatorValidator(), "./data/utilizatori.txt");
        Utilizator u1 = new Utilizator("IOaNUT", "a");
        Utilizator u2 = new Utilizator("Eihdadaai", "b");
        Utilizator u3 = null;
        u1.setId(4L);
        u2.setId(5L);

        Utilizator updater = new Utilizator("updater", "g");
        updater.setId(8L);


        try {
            repoFile.save(u1);
            repoFile.save(u2);
            repoFile.save(u3);
        }catch(IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
        }catch(ValidationException e)
        {
            System.out.println(e.getMessage());
        }
        System.out.println();


        repoFile.update(updater);
        repoFile.delete(1L);
        Prietenie p = new Prietenie();
        Prietenie p2 = new Prietenie();
        p.setId(new Tuplu<>(3L, 8L));
        p2.setId(new Tuplu<>(8L, 3L));
        System.out.println(p.getId().equals(p2.getId()));
*/

        Validator<Utilizator> utilizatorValidator = new UtilizatorValidator();
        Repository<Long, Utilizator> utilizatorRepository = new UtilizatorRepository(utilizatorValidator, "./data/utilizatori.txt");

        Validator<Prietenie> prietenieValidator = new PrietenieValidator();
        Repository<Tuplu<Long, Long>, Prietenie> prietenieRepository = new PrietenieRepository(prietenieValidator, "./data/prietenii.txt");

        UtilizatorService utilizatorService = new UtilizatorService(utilizatorRepository);
        PrietenieService prietenieService = new PrietenieService(prietenieRepository, utilizatorRepository);
        ConsoleUI consoleUI = new ConsoleUI(utilizatorService, prietenieService);
        consoleUI.start();
    }
}