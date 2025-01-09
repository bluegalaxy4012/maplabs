using System;
using System.Collections.Generic;
using System.IO;
using Lab10.Domain.Validators;

namespace Lab10.Domain;

public abstract class AbstractFileRepository<ID, E> : InMemoryRepository<ID, E> where E : Entity<ID>
{
    private readonly string filename;

    public AbstractFileRepository(IValidator<E> validator, string fileName) : base(validator)
    {
        filename = fileName;
    }

    protected void Initialize()
    {
        ReadFromFile();
    }

    public abstract E CreateEntity(string line);

    public abstract string SaveEntity(E entity);

    public override E Save(E entity)
    {
        var e = base.Save(entity);
        if (e == null) WriteToFile();
        return e;
    }

    private void WriteToFile()
    {
        try
        {
            using (var writer = new StreamWriter(filename))
            {
                foreach (var entity in entities.Values)
                {
                    var ent = SaveEntity(entity);
                    writer.WriteLine(ent);
                }
            }
        }
        catch (IOException e)
        {
            throw new Exception("Error writing to file", e);
        }
    }

    private void ReadFromFile()
    {
        try
        {
            using (var reader = new StreamReader(filename))
            {
                string line;
                while ((line = reader.ReadLine()) != null)
                {
                    var entity = CreateEntity(line);
                    base.Save(entity);
                }
            }
        }
        catch (IOException e)
        {
            throw new Exception("Error reading from file", e);
        }
    }

    public override E Delete(ID id)
    {
        var e = base.Delete(id);
        WriteToFile();
        return e;
    }

    public override E Update(E entity)
    {
        var e = base.Update(entity);
        WriteToFile();
        return e;
    }
}