package ubb.scs.map.domain.validators;


import ubb.scs.map.domain.Utilizator;

public class UtilizatorValidator implements Validator<Utilizator> {
    @Override
    public void validate(Utilizator entity) throws ValidationException {
        if (entity.getFirstName().length() <= 1 || entity.getLastName().length() <= 1 || !(entity.getId() >= 0))
            throw new ValidationException("Utilizatorul nu este valid.");
    }
}
