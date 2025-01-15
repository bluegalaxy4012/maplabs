package ubb.scs.map.repository.database;

import ubb.scs.map.domain.Entity;
import ubb.scs.map.domain.validators.Validator;
import ubb.scs.map.repository.PagingRepository;
import ubb.scs.map.utils.paging.Page;
import ubb.scs.map.utils.paging.Pageable;

import java.sql.*;
import java.util.*;

public abstract class AbstractDbRepository<ID, E extends Entity<ID>> implements PagingRepository<ID, E> {
    protected final String url;
    protected final String username;
    protected final String password;
    protected final Validator<E> validator;
    protected Connection connection;

    public AbstractDbRepository(String url, String username, String password, Validator<E> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
        try {
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected abstract E extractEntity(ResultSet resultSet) throws SQLException;

    protected abstract void setEntityParameters(PreparedStatement statement, E entity) throws SQLException;

    protected abstract String getTableName();

    protected abstract String getPrimaryKeyCondition();

    protected abstract void setPrimaryKeyParameters(PreparedStatement statement, ID id) throws SQLException;

    protected abstract String getColumnNames();

    protected abstract int getColumnCount();

    @Override
    public Optional<E> findOne(ID id) {
        E entity = null;
        String query = "SELECT * FROM " + getTableName() + " WHERE " + getPrimaryKeyCondition();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            setPrimaryKeyParameters(statement, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                entity = extractEntity(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(entity);
    }

    @Override
    public Iterable<E> findAll() {
        Set<E> entities = new HashSet<>();
        String query = "SELECT * FROM " + getTableName();
        try (PreparedStatement statement = connection.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                entities.add(extractEntity(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entities;
    }

    @Override
    public Optional<E> save(E entity) {
        validator.validate(entity);
        String query = "INSERT INTO " + getTableName() + " (" + getColumnNames() + ") VALUES (" + "?,".repeat(getColumnCount() - 1) + "?)";
        int rez = -1;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            setEntityParameters(statement, entity);
            rez = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (rez > 0) return Optional.empty();
        else return Optional.of(entity);
    }

    @Override
    public Optional<E> delete(ID id) {
        Optional<E> entity = findOne(id);
        if (entity.isEmpty()) return Optional.empty();
        String query = "DELETE FROM " + getTableName() + " WHERE " + getPrimaryKeyCondition();
        int rez = -1;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            setPrimaryKeyParameters(statement, id);
            rez = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (rez > 0) return entity;
        else return Optional.empty();
    }

    @Override
    public Optional<E> update(E entity) {
        validator.validate(entity);
        String[] columns = getColumnNames().split(", ");
        String setClause = String.join(" = ?, ", columns) + " = ?";
        String query = "UPDATE " + getTableName() + " SET " + setClause + " WHERE " + getPrimaryKeyCondition();
        int rez = -1;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            setEntityParameters(statement, entity);
            statement.setLong(getColumnCount() + 1, (long) entity.getId());
            rez = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (rez > 0) return Optional.empty();
        else return Optional.of(entity);
    }

    @Override
    public Page<E> findAllOnPage(Pageable pageable) {
        List<E> entitiesOnPage = new ArrayList<>();
        String query = "SELECT * FROM " + getTableName() + " LIMIT ? OFFSET ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, pageable.getPageSize());
            statement.setInt(2, pageable.getPageSize() * pageable.getPageNumber());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                entitiesOnPage.add(extractEntity(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int totalNumberOfElements = countTotalElements();
        return new Page<>(entitiesOnPage, totalNumberOfElements);
    }

    private int countTotalElements() {
        String query = "SELECT COUNT(*) FROM " + getTableName();
        try (PreparedStatement statement = connection.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}