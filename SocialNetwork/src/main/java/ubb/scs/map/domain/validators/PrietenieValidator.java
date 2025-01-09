package ubb.scs.map.domain.validators;

import ubb.scs.map.domain.Prietenie;

public class PrietenieValidator implements Validator<Prietenie> {
    @Override
    public void validate(Prietenie entity) throws ValidationException {
        if (entity.getTuplu().getE1().equals(entity.getTuplu().getE2()) || !(entity.getTuplu().getE1() >= 0) || !(entity.getTuplu().getE2() >= 0))
            throw new ValidationException("Prietenie invalida.");
    }
}
