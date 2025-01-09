// See https://aka.ms/new-console-template for more information

using System;
using System.Collections.Generic;
using System.Linq;
using Lab10.Domain;
using Lab10.Domain.Validators;
using Lab10.Repository;

namespace Lab10;

internal class Program
{
    private static void Main(string[] args)
    {
        var path = Environment.CurrentDirectory;

        var echipaRepository = new EchipaRepository(new EchipaValidator(), Path.Combine(path, "echipe.txt"));
        var jucatorRepository =
            new JucatorRepository(new JucatorValidator(), Path.Combine(path, "jucatori.txt"), echipaRepository);
        var jucatorActivRepository =
            new JucatorActivRepository(new JucatorActivValidator(), Path.Combine(path, "jucatori_activi.txt"));
        var meciRepository =
            new MeciRepository(new MeciValidator(), Path.Combine(path, "meciuri.txt"), echipaRepository);

        List<string> scoalaList = new List<string>
        {
            "Scoala Gimnaziala Horea",
            "Scoala Gimnaziala Octavian Goga",
            "Liceul Teoretic Lucian Blaga",
            "Scoala Gimnaziala Ioan Bob",
            "Scoala Gimnaziala Ion Creanga",
            "Colegiul National Pedagogic Gheorghe Lazar",
            "Scoala Gimnaziala Internationala SPECTRUM",
            "Colegiul National Emil Racovita",
            "Colegiul National George Cosbuc",
            "Scoala Gimnaziala Ion Agarbiceanu",
            "Liceul Teoretic Avram Iancu",
            "Scoala Gimnaziala Constantin Brancusi",
            "Liceul Teoretic Onisifor Ghibu",
            "Liceul cu Program Sportiv Cluj-Napoca",
            "Liceul Teoretic Nicolae Balcescu",
            "Liceul Teoretic Gheorghe Sincai",
            "Scoala Nicolae Titulescu",
            "Scoala Gimnaziala Liviu Rebreanu",
            "Scoala Gimnaziala Iuliu Hatieganu",
            "Liceul Teoretic Bathory Istvan",
            "Colegiul National George Baritiu",
            "Liceul Teoretic Apaczai Csere Janos",
            "Seminarul Teologic Ortodox",
            "Liceul de Informatica Tiberiu Popoviciu",
            "Scoala Gimnaziala Alexandru Vaida – Voevod",
            "Liceul Teoretic ELF",
            "Scoala Gimnaziala Gheorghe Sincai Floresti"
        };


        List<string> echipaList = new List<string>
        {
            "Houston Rockets",
            "Los Angeles Lakers",
            "LA Clippers",
            "Chicago Bulls",
            "Cleveland Cavaliers",
            "Utah Jazz",
            "Brooklyn Nets",
            "New Orleans Pelicans",
            "Indiana Pacers",
            "Toronto Raptors",
            "Charlotte Hornets",
            "Phoenix Suns",
            "Portland TrailBlazers",
            "Golden State Warriors",
            "Washington Wizards",
            "San Antonio Spurs",
            "Orlando Magic",
            "Denver Nuggets",
            "Detroit Pistons",
            "Atlanta Hawks",
            "Dallas Mavericks",
            "Sacramento Kings",
            "Oklahoma City Thunder",
            "Boston Celtics",
            "New York Knicks",
            "Minnesota Timberwolves",
            "Miami Heat",
            "Milwaukee Bucks"
        };


        List<string> numeList = new List<string>
        {
            "Ion",
            "Vasile",
            "Gheorghe",
            "Mihai",
            "Andrei",
            "Cristian",
            "Florin",
            "Alexandru",
            "George",
            "Marius",
            "Adrian",
            "Dan",
            "Dumitru",
            "Costel",
            "Flaviu"
        };

        echipaList.ForEach(echipa => echipaRepository.Save(new Echipa(echipa) { Id = echipaList.IndexOf(echipa) + 1 }));


        for (var i = 0; i < 4; i++)
        {
            numeList = numeList.OrderBy(x => Guid.NewGuid()).ToList();


            for (var j = 0; j < numeList.Count; j++)
            {
                var echipai = echipaRepository.FindOne(i + 1);
                Jucator jucator;
                if (j < 3)
                    jucator = new Jucator(numeList[j], scoalaList[i], echipai);
                else
                    jucator = new Jucator(numeList[j], scoalaList[i], null);

                jucator.Id = i * numeList.Count + j + 1;
                jucatorRepository.Save(jucator);
            }
        }


        List<Tuple<int, int>> perechi = new()
        {
            Tuple.Create(1, 2),
            Tuple.Create(2, 3),
            Tuple.Create(1, 4),
            Tuple.Create(2, 4),
            Tuple.Create(2, 3),
            Tuple.Create(1, 3)
        };


        for (var i = 0; i < perechi.Count; i++)
        {
            //data random
            var random = new Random();
            var rndstart = new DateTime(2024, 1, 1);
            var rndsfarsit = new DateTime(2026, 1, 1);
            var range = rndsfarsit - rndstart;
            var randTimeSpan = new TimeSpan((long)(random.NextDouble() * range.Ticks));

            var mecii = new Meci(echipaRepository.FindOne(perechi[i].Item1), echipaRepository.FindOne(perechi[i].Item2),
                rndstart + randTimeSpan);
            mecii.Id = i + 1;
            meciRepository.Save(mecii);
        }


        //primul meci
        jucatorActivRepository.Save(new JucatorActiv(1, 1, 0, "Portar") { Id = 1 });
        ;
        jucatorActivRepository.Save(new JucatorActiv(2, 1, 3, "Atacant") { Id = 2 });
        jucatorActivRepository.Save(new JucatorActiv(16, 1, 2, "Portar") { Id = 3 });
        jucatorActivRepository.Save(new JucatorActiv(17, 1, 4, "Mijlocas") { Id = 4 });

        //al doilea meci
        jucatorActivRepository.Save(new JucatorActiv(17, 2, 1, "Portar") { Id = 5 });
        jucatorActivRepository.Save(new JucatorActiv(18, 2, 3, "Atacant") { Id = 6 });
        jucatorActivRepository.Save(new JucatorActiv(31, 2, 2, "Portar") { Id = 7 });
        jucatorActivRepository.Save(new JucatorActiv(32, 2, 9, "Mijlocas") { Id = 8 });

        //al treilea meci
        jucatorActivRepository.Save(new JucatorActiv(2, 3, 0, "Portar") { Id = 9 });
        jucatorActivRepository.Save(new JucatorActiv(3, 3, 5, "Atacant") { Id = 10 });
        jucatorActivRepository.Save(new JucatorActiv(46, 3, 2, "Portar") { Id = 11 });
        jucatorActivRepository.Save(new JucatorActiv(47, 3, 6, "Mijlocas") { Id = 12 });

        //altele

        var service = new Service(jucatorRepository, jucatorActivRepository, meciRepository);

        // jucatori unei echipe date
        var echipa = echipaRepository.FindOne(1);
        var jucatori = service.GetJucatoriByEchipa(echipa);
        Console.WriteLine("Jucatorii echipei " + echipa.Nume + " sunt:");
        foreach (var jucator in jucatori) Console.WriteLine(jucator);
        Console.WriteLine("--------------------");


        //toti jucatorii activi ai unei echipe date de la un anumit meci
        var meci = meciRepository.FindOne(1);
        var jucatoriActivi = service.GetJucatoriActiviByEchipaAndMeci(echipa, meci);
        Console.WriteLine("Jucatorii activi ai echipei " + echipa.Nume + " la meciul " + meci.Id + " sunt:");
        foreach (var jucatorActiv in jucatoriActivi) Console.WriteLine(jucatorActiv);
        Console.WriteLine("--------------------");

        //meciuri dintr-o perioada calendaristica
        var start = new DateTime(2024, 1, 1);
        var sfarsit = new DateTime(2024, 12, 31);
        var meciuri = service.GetMeciuriByPeriod(start, sfarsit);
        Console.WriteLine("Meciurile din perioada " + start + " - " + sfarsit + " sunt:");
        foreach (var meci1 in meciuri) Console.WriteLine(meci1);
        Console.WriteLine("--------------------");

        //scorul unui meci
        var scor = service.GetScorByMeci(meci);
        Console.WriteLine("Scorul meciului " + meci.Id + " este:");
        Console.WriteLine(scor.Item1 + " - " + scor.Item2);
    }
}