using Lab10.Domain;
using Lab10.Domain.Validators;

namespace Lab10.Repository;

public class JucatorActivRepository : AbstractFileRepository<int, JucatorActiv>
{
    public JucatorActivRepository(IValidator<JucatorActiv> validator, string fileName) : base(validator, fileName)
    {
        Initialize();
    }

    public override JucatorActiv CreateEntity(string line)
    {
        var fields = line.Split(",");
        var id = int.Parse(fields[0]);
        var idJucator = int.Parse(fields[1]);
        var idMeci = int.Parse(fields[2]);
        var nrPuncteInscrise = int.Parse(fields[3]);
        var Tip = fields[4];

        return new JucatorActiv(idJucator, idMeci, nrPuncteInscrise, Tip) { Id = id };
    }

    public override string SaveEntity(JucatorActiv entity)
    {
        return $"{entity.Id},{entity.IdJucator},{entity.IdMeci},{entity.NrPuncteInscrise},{entity.Tip}";
    }
}