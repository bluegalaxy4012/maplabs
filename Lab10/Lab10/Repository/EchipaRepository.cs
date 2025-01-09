using Lab10.Domain;
using Lab10.Domain.Validators;

namespace Lab10.Repository;

public class EchipaRepository : AbstractFileRepository<int, Echipa>
{
    public EchipaRepository(IValidator<Echipa> validator, string fileName) : base(validator, fileName)
    {
        Initialize();
    }

    public override Echipa CreateEntity(string line)
    {
        var fields = line.Split(",");
        var id = int.Parse(fields[0]);
        var nume = fields[1];
        return new Echipa(nume) { Id = id };
    }

    public override string SaveEntity(Echipa entity)
    {
        return $"{entity.Id},{entity.Nume}";
    }
}