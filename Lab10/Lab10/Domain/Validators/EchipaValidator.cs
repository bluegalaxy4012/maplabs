namespace Lab10.Domain.Validators;

public class EchipaValidator : IValidator<Echipa>
{
    public void Validate(Echipa entity)
    {
        if (entity.Id < 0)
        {
            throw new ValidationException("Id-ul nu poate fi negativ");
        }
        if (entity.Nume == null || entity.Nume.Equals(""))
        {
            throw new ValidationException("Numele echipei nu poate fi vid");
        }
        if (entity.Nume.Length < 3)
        {
            throw new ValidationException("Numele echipei trebuie sa aiba cel putin 3 caractere");
        }
    }
}