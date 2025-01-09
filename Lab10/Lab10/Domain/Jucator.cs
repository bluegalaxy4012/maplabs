namespace Lab10.Domain;

public class Jucator : Elev
{
    public Echipa Echipa { get; set; }

    public Jucator(string nume, string scoala, Echipa echipa) : base(nume, scoala)
    {
        Echipa = echipa;
    }

    public override string ToString()
    {
        return $"Jucatorul {Nume} de la echipa {Echipa.Nume}";
    }
}