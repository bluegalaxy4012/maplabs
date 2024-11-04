package ubb.scs.map.repository.database;

import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Tuplu;
import ubb.scs.map.domain.validators.Validator;
import ubb.scs.map.repository.Repository;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class PrietenieDbRepository implements Repository<Tuplu<Long, Long>, Prietenie> {
    private final String url;
    private final String username;
    private final String password;
    private final Validator<Prietenie> validator;

    public PrietenieDbRepository(String url, String username, String password, Validator<Prietenie> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Optional<Prietenie> findOne(Tuplu<Long, Long> id) {
        Prietenie prietenie = null;
        try (Connection connection = DriverManager.getConnection(url, username, password); PreparedStatement statement = connection.prepareStatement("SELECT * FROM Prietenii WHERE id1 = ? AND id2 = ?")) {
            statement.setLong(1, id.getE1());
            statement.setLong(2, id.getE2());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                prietenie = new Prietenie();
                prietenie.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(prietenie);
    }

    @Override
    public Iterable<Prietenie> findAll() {
        Set<Prietenie> prietenii = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password); PreparedStatement statement = connection.prepareStatement("SELECT * FROM Prietenii"); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");
                Prietenie prietenie = new Prietenie();
                prietenie.setId(new Tuplu<>(id1, id2));
                prietenii.add(prietenie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prietenii;
    }

    @Override
    public Optional<Prietenie> save(Prietenie entity) {
        validator.validate(entity);
        int rez = -1;
        try (Connection connection = DriverManager.getConnection(url, username, password); PreparedStatement statement = connection.prepareStatement("INSERT INTO Prietenii (id1, id2) VALUES (?, ?)")) {
            statement.setLong(1, entity.getId().getE1());
            statement.setLong(2, entity.getId().getE2());
            rez = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (rez > 0) return Optional.empty();
        else return Optional.of(entity);
    }

    @Override
    public Optional<Prietenie> delete(Tuplu<Long, Long> id) {
        Prietenie prietenie = findOne(id).orElse(null);
        if (prietenie == null) {
            return Optional.empty();
        }
        int rez = -1;
        try (Connection connection = DriverManager.getConnection(url, username, password); PreparedStatement statement = connection.prepareStatement("DELETE FROM Prietenii WHERE id1 = ? AND id2 = ?")) {
            statement.setLong(1, id.getE1());
            statement.setLong(2, id.getE2());
            rez = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (rez > 0) return Optional.of(prietenie);
        else return Optional.empty();
    }

    @Override
    public Optional<Prietenie> update(Prietenie entity) {
        validator.validate(entity);
        int rez = -1;
        try (Connection connection = DriverManager.getConnection(url, username, password); PreparedStatement statement = connection.prepareStatement("UPDATE Prietenii SET id1 = ?, id2 = ? WHERE id1 = ? AND id2 = ?")) {
            statement.setLong(1, entity.getId().getE1());
            statement.setLong(2, entity.getId().getE2());
            statement.setLong(3, entity.getId().getE1());
            statement.setLong(4, entity.getId().getE2());
            rez = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (rez > 0) return Optional.empty();
        else return Optional.of(entity);
    }
}