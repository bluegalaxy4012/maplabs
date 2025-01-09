namespace Lab10.Domain;

public class JucatorActiv : Entity<int>
{
    public int IdJucator { get; set; }
    public int IdMeci { get; set; }
    public int NrPuncteInscrise { get; set; }
    public string Tip { get; set; }

    public JucatorActiv(int idJucator, int idMeci, int nrPuncteInscrise, string tip)
    {
        IdJucator = idJucator;
        IdMeci = idMeci;
        NrPuncteInscrise = nrPuncteInscrise;
        Tip = tip;
    }

    public override string ToString()
    {
        return
            $"Jucatorul activ cu IdJucator {IdJucator} a inscris {NrPuncteInscrise} puncte in meciul cu IdMeci {IdMeci}";
    }
}