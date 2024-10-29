package ubb.scs.map.repository.file;

import ubb.scs.map.domain.Entity;
import ubb.scs.map.domain.validators.Validator;
import ubb.scs.map.repository.memory.InMemoryRepository;

import java.io.*;

public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {
    private final String filename;

    public AbstractFileRepository(Validator<E> validator, String fileName) {
        super(validator);
        filename = fileName;
        readFromFile();
    }


    public abstract E createEntity(String line);

    public abstract String saveEntity(E entity);

    @Override
    public E save(E entity) {
        E e = super.save(entity);
        if (e == null) writeToFile();
        return e;
    }

    private void writeToFile() {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (E entity : entities.values()) {
                String ent = saveEntity(entity);
                writer.write(ent);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void readFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                E entity = createEntity(line);
                super.save(entity);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public E delete(ID id) {
        E entity = super.delete(id);
        writeToFile(); //nu i null
        return entity;
    }

    @Override
    public E update(E entity) {
        E _entity = super.update(entity);
        writeToFile(); // la fel
        return _entity;
    }
}
