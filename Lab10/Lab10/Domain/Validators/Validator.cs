namespace Lab10.Domain.Validators;

public interface IValidator<T>
{
    void Validate(T entity);
}