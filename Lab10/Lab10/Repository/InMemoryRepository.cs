using System;
using System.Collections.Generic;
using System.Linq;
using Lab10.Domain.Validators;
using Lab10.Repository;

namespace Lab10.Domain;

public class InMemoryRepository<ID, E> : IRepository<ID, E> where E : Entity<ID>
{
    private readonly IValidator<E> validator;
    protected Dictionary<ID, E> entities;

    public InMemoryRepository(IValidator<E> validator)
    {
        this.validator = validator;
        entities = new Dictionary<ID, E>();
    }

    public E FindOne(ID id)
    {
        if (id == null) throw new ArgumentException("ID CANNOT BE NULL");
        entities.TryGetValue(id, out var entity);
        return entity;
    }

    public IEnumerable<E> FindAll()
    {
        return entities.Values;
    }

    public virtual E Save(E entity)
    {
        if (entity == null) throw new ArgumentException("ENTITY CANNOT BE NULL");
        validator.Validate(entity);
        if (entities.ContainsKey(entity.Id)) return entity;
        entities[entity.Id] = entity;
        return null;
    }

    public virtual E Delete(ID id)
    {
        if (id == null) throw new ArgumentException("ID CANNOT BE NULL");
        if (entities.TryGetValue(id, out var entity))
        {
            entities.Remove(id);
            return entity;
        }

        return null;
    }

    public virtual E Update(E entity)
    {
        if (entity == null) throw new ArgumentException("ENTITY CANNOT BE NULL");
        validator.Validate(entity);
        if (entities.ContainsKey(entity.Id))
        {
            entities[entity.Id] = entity;
            return null;
        }

        return entity;
    }
}