namespace Lab10.Domain;

public class Elev : Entity<int>
{
    public string Nume { get; set; }
    public string Scoala { get; set; }

    public Elev(string nume, string scoala)
    {
        Nume = nume;
        Scoala = scoala;
    }

    public override string ToString()
    {
        return $"Elevul {Nume} de la scoala {Scoala}";
    }
}