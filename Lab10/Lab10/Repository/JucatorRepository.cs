using Lab10.Domain;
using Lab10.Domain.Validators;

namespace Lab10.Repository;

public class JucatorRepository : AbstractFileRepository<int, Jucator>
{
    private readonly EchipaRepository echipaRepository;

    //un elev e un jucator cu echipa nula
    public JucatorRepository(IValidator<Jucator> validator, string fileName, EchipaRepository echipaRepository) : base(
        validator, fileName)
    {
        this.echipaRepository = echipaRepository;
        Initialize();
    }


    public override Jucator CreateEntity(string line)
    {
        var fields = line.Split(",");
        var id = int.Parse(fields[0]);
        var nume = fields[1];
        var scoala = fields[2];
        var idEchipa = int.Parse(fields[3]);
        var echipa = echipaRepository.FindOne(idEchipa);
        return new Jucator(nume, scoala, echipa) { Id = id };
    }

    public override string SaveEntity(Jucator entity)
    {
        if (entity.Echipa == null)
            return $"{entity.Id},{entity.Nume},{entity.Scoala},0";

        return $"{entity.Id},{entity.Nume},{entity.Scoala},{entity.Echipa.Id}";
    }
}