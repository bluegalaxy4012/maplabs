package ubb.scs.map.repository.database;

import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.domain.validators.Validator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtilizatorDbRepository extends AbstractDbRepository<Long, Utilizator> {

    public UtilizatorDbRepository(String url, String username, String password, Validator<Utilizator> validator) {
        super(url, username, password, validator);
    }

    @Override
    protected Utilizator extractEntity(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        Utilizator utilizator = new Utilizator(firstName, lastName);
        utilizator.setId(id);
        return utilizator;
    }

    @Override
    protected void setEntityParameters(PreparedStatement statement, Utilizator entity) throws SQLException {
        statement.setString(1, entity.getFirstName());
        statement.setString(2, entity.getLastName());
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
        return "first_name, last_name";
    }
}