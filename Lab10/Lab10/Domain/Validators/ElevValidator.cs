namespace Lab10.Domain.Validators;

public class ElevValidator : IValidator<Elev>
{
    public void Validate(Elev entity)
    {
        if(entity.Id < 0)
            throw new ValidationException("Id-ul nu poate fi negativ");
        
        if(entity.Nume == null || entity.Nume.Equals(""))
            throw new ValidationException("Numele elevului nu poate fi vid");
        
        if(entity.Nume.Length < 3)
            throw new ValidationException("Numele elevului trebuie sa aiba cel putin 3 caractere");
        
        if(entity.Scoala == null || entity.Scoala.Equals(""))
            throw new ValidationException("Scoala elevului nu poate fi vida");
        
        if(entity.Scoala.Length < 3)
            throw new ValidationException("Scoala elevului trebuie sa aiba cel putin 3 caractere");
    }
}