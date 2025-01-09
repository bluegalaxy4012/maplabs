namespace Lab10.Domain;

public class Meci : Entity<int>
{
    public Echipa Echipa1 { get; set; }
    public Echipa Echipa2 { get; set; }
    public DateTime Data { get; set; }

    public Meci(Echipa echipa1, Echipa echipa2, DateTime data)
    {
        Echipa1 = echipa1;
        Echipa2 = echipa2;
        Data = data;
    }

    public override string ToString()
    {
        return $"{Echipa1.Nume} vs {Echipa2.Nume} - {Data}";
    }
}