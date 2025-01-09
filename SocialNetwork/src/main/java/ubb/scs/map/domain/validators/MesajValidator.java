package ubb.scs.map.domain.validators;

import ubb.scs.map.domain.Mesaj;

public class MesajValidator implements Validator<Mesaj> {
    @Override
    public void validate(Mesaj entity) throws ValidationException {
        if (entity.getFrom() == null || entity.getTo() == null || entity.getText() == null || entity.getDate() == null)
            throw new ValidationException("Mesaj invalid.");
    }
}
