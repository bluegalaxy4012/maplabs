namespace Lab10.Domain.Validators;

public class JucatorActivValidator : IValidator<JucatorActiv>
{
    public void Validate(JucatorActiv entity)
    {
        if (entity.IdJucator < 0)
        {
            throw new ValidationException("Id-ul nu poate fi negativ");
        }
        if (entity.IdJucator < 0)
        {
            throw new ValidationException("Id-ul jucatorului nu poate fi negativ");
        }
        if (entity.NrPuncteInscrise < 0)
        {
            throw new ValidationException("Id-ul echipei nu poate fi negativ");
        }
        if (entity.Tip == null || entity.Tip.Equals(""))
        {
            throw new ValidationException("Tipul jucatorului nu poate fi vid");
        }

        if (entity.Tip.Length < 3)
        {
            throw new ValidationException("Tipul jucatorului trebuie sa aiba cel putin 3 caractere");
        }
    }
}