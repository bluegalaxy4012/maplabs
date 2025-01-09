namespace Lab10.Domain;

public class Echipa : Entity<int>
{
    public string Nume { get; set; }

    public Echipa(string nume)
    {
        Nume = nume;
    }

    public override string ToString()
    {
        return $"Echipa {Nume}";
    }
}