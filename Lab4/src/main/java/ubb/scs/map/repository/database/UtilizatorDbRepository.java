package ubb.scs.map.repository.database;


import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.domain.validators.Validator;
import ubb.scs.map.repository.Repository;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UtilizatorDbRepository implements Repository<Long, Utilizator> {
    private final String url;
    private final String username;
    private final String password;
    private final Validator<Utilizator> validator;

    public UtilizatorDbRepository(String url, String username, String password, Validator<Utilizator> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Optional<Utilizator> findOne(Long id) {
        Utilizator utilizator = null;
        try (Connection connection = DriverManager.getConnection(url, username, password); PreparedStatement statement = connection.prepareStatement("SELECT * from Utilizatori where id = ?")) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                utilizator = new Utilizator(firstName, lastName);
                utilizator.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(utilizator);
    }

    @Override
    public Optional<Utilizator> update(Utilizator entity) {
        validator.validate(entity);
        int rez = -1;
        try (Connection connection = DriverManager.getConnection(url, username, password); PreparedStatement statement = connection.prepareStatement("UPDATE Utilizatori SET first_name = ?, last_name = ? WHERE id = ?")) {
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setLong(3, entity.getId());
            rez = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (rez > 0) return Optional.empty();
        else return Optional.of(entity);
    }

    @Override
    public Optional<Utilizator> delete(Long id) {
        Utilizator utilizator = findOne(id).get();
        int rez = -1;
        try (Connection connection = DriverManager.getConnection(url, username, password); PreparedStatement statement = connection.prepareStatement("DELETE FROM Utilizatori WHERE id = ?")) {
            statement.setLong(1, id);
            rez = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();

        }
        if (rez > 0) return Optional.of(utilizator);
        else return Optional.empty();
    }

    @Override
    public Iterable<Utilizator> findAll() {
        Set<Utilizator> utilizatori = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password); PreparedStatement statement = connection.prepareStatement("SELECT * from Utilizatori"); ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");

                Utilizator utilizator = new Utilizator(firstName, lastName);
                utilizator.setId(id);
                utilizatori.add(utilizator);
            }
            return utilizatori;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilizatori;
    }

    @Override
    public Optional<Utilizator> save(Utilizator entity) {
        validator.validate(entity);
        int rez = -1;
        try (Connection connection = DriverManager.getConnection(url, username, password); PreparedStatement statement = connection.prepareStatement("INSERT INTO Utilizatori (first_name, last_name) VALUES (?, ?)")) {
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            rez = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (rez > 0) return Optional.empty();
        else return Optional.of(entity);
    }


}