using System;
using System.Collections.Generic;
using System.Linq;
using Lab10.Repository;

namespace Lab10.Domain;

public class Service
{
    private readonly JucatorRepository jucatorRepository;
    private readonly JucatorActivRepository jucatorActivRepository;
    private readonly MeciRepository meciRepository;

    public Service(JucatorRepository jucatorRepo, JucatorActivRepository jucatorActivRepo, MeciRepository meciRepo)
    {
        jucatorRepository = jucatorRepo;
        jucatorActivRepository = jucatorActivRepo;
        meciRepository = meciRepo;
    }

    public IEnumerable<Jucator> GetJucatoriByEchipa(Echipa echipa)
    {
        return jucatorRepository.FindAll().Where(j => j.Echipa != null && j.Echipa.Id == echipa.Id);
    }

    public IEnumerable<Jucator> GetJucatoriActiviByEchipaAndMeci(Echipa echipa, Meci meci)
    {
        var jucatoriposibili = GetJucatoriByEchipa(echipa);
        var jucatoriactivi = jucatorActivRepository.FindAll()
            .Where(ja => jucatoriposibili.Any(j => j.Id == ja.IdJucator) && ja.IdMeci == meci.Id);

        return jucatoriactivi.Select(ja => jucatorRepository.FindOne(ja.IdJucator));
    }

    public IEnumerable<Meci> GetMeciuriByPeriod(DateTime start, DateTime end)
    {
        return meciRepository.FindAll().Where(m => m.Data >= start && m.Data <= end);
    }

    public Tuple<int, int> GetScorByMeci(Meci meci)
    {
        var jucatoriactivi = jucatorActivRepository.FindAll().Where(ja => ja.IdMeci == meci.Id);
        var scor = jucatoriactivi.Aggregate(Tuple.Create(0, 0), (acc, ja) =>
        {
            var jucator = jucatorRepository.FindOne(ja.IdJucator);
            if (jucator.Echipa.Id == meci.Echipa1.Id)
                return Tuple.Create(acc.Item1 + ja.NrPuncteInscrise, acc.Item2);
            return Tuple.Create(acc.Item1, acc.Item2 + ja.NrPuncteInscrise);
        });

        return scor;
    }
}