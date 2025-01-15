package ubb.scs.map.repository.database;

import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.domain.validators.Validator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UtilizatorDbRepository extends AbstractDbRepository<Long, Utilizator> {

    public UtilizatorDbRepository(String url, String username, String password, Validator<Utilizator> validator) {
        super(url, username, password, validator);
    }

    @Override
    protected Utilizator extractEntity(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String username = resultSet.getString("username");
        String hashedPassword = resultSet.getString("hashed_password");
        String profilePictureUrl = resultSet.getString("profile_picture_url");
        Utilizator utilizator = new Utilizator(firstName, lastName, username, hashedPassword);
        utilizator.setId(id);
        utilizator.setProfilePictureUrl(profilePictureUrl);
        return utilizator;
    }

    @Override
    protected void setEntityParameters(PreparedStatement statement, Utilizator entity) throws SQLException {
        statement.setString(1, entity.getFirstName());
        statement.setString(2, entity.getLastName());
        statement.setString(3, entity.getUsername());
        statement.setString(4, entity.getHashedPassword());
        statement.setString(5, entity.getProfilePictureUrl());
    }

    @Override
    protected String getTableName() {
        return "Utilizatori";
    }

    @Override
    protected String getPrimaryKeyCondition() {
        return "id = ?";
    }

    @Override
    protected void setPrimaryKeyParameters(PreparedStatement statement, Long id) throws SQLException {
        statement.setLong(1, id);
    }

    @Override
    protected String getColumnNames() {
        return "first_name, last_name, username, hashed_password, profile_picture_url";
    }

    @Override
    protected int getColumnCount() {
        return 5;
    }

    public Optional<Utilizator> findByLogin(String username, String hashedPassword) {
        String query = "SELECT * FROM " + getTableName() + " WHERE username = ? AND hashed_password = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, hashedPassword);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(extractEntity(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

}