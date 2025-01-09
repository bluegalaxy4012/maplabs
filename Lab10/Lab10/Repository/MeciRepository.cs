using Lab10.Domain;
using Lab10.Domain.Validators;

namespace Lab10.Repository;

public class MeciRepository : AbstractFileRepository<int, Meci>
{
    private readonly EchipaRepository echipaRepository;

    public MeciRepository(IValidator<Meci> validator, string fileName, EchipaRepository echipaRepository) : base(
        validator, fileName)
    {
        this.echipaRepository = echipaRepository;
        Initialize();
    }

    public override Meci CreateEntity(string line)
    {
        var fields = line.Split(",");
        var id = int.Parse(fields[0]);
        var idEchipa1 = int.Parse(fields[1]);
        var idEchipa2 = int.Parse(fields[2]);
        var data = DateTime.Parse(fields[3]);

        var echipa1 = echipaRepository.FindOne(idEchipa1);
        var echipa2 = echipaRepository.FindOne(idEchipa2);

        return new Meci(echipa1, echipa2, data) { Id = id };
    }

    public override string SaveEntity(Meci entity)
    {
        return $"{entity.Id},{entity.Echipa1.Id},{entity.Echipa2.Id},{entity.Data}";
    }
}