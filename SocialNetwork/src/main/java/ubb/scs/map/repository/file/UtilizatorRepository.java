package ubb.scs.map.repository.file;

import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.domain.validators.Validator;

public class UtilizatorRepository extends AbstractFileRepository<Long, Utilizator> {
    public UtilizatorRepository(Validator<Utilizator> validator, String fileName) {
        super(validator, fileName);
    }

    @Override
    public Utilizator createEntity(String line) {
        String[] splited = line.split(";");
        Utilizator u = new Utilizator(splited[1], splited[2], splited[3], splited[4]);
        u.setId(Long.parseLong(splited[0]));
        return u;
    }

    @Override
    public String saveEntity(Utilizator entity) {
        return entity.getId() + ";" + entity.getFirstName() + ";" + entity.getLastName() + ";" + entity.getUsername() + ";" + entity.getHashedPassword();
    }
}
