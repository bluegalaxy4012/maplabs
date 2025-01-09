package ubb.scs.map.repository.file;

import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Tuplu;
import ubb.scs.map.domain.validators.Validator;

public class PrietenieRepository extends AbstractFileRepository<Tuplu<Long, Long>, Prietenie> {
    public PrietenieRepository(Validator<Prietenie> validator, String fileName) {
        super(validator, fileName);
    }

    @Override
    public Prietenie createEntity(String line) {
        String[] splited = line.split(";");
        var id1 = Long.valueOf(splited[0]);
        var id2 = Long.valueOf(splited[1]);

        Prietenie p = new Prietenie();
        p.setId(new Tuplu<>(id1, id2));
        return p;
    }

    @Override
    public String saveEntity(Prietenie entity) {
        return entity.getId().getE1() + ";" + entity.getId().getE2();
    }
}