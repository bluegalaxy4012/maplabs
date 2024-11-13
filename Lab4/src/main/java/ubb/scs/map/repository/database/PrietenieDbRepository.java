package ubb.scs.map.repository.database;

import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Tuplu;
import ubb.scs.map.domain.validators.Validator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PrietenieDbRepository extends AbstractDbRepository<Tuplu<Long, Long>, Prietenie> {

    public PrietenieDbRepository(String url, String username, String password, Validator<Prietenie> validator) {
        super(url, username, password, validator);
    }

    @Override
    protected Prietenie extractEntity(ResultSet resultSet) throws SQLException {
        Long id1 = resultSet.getLong("id1");
        Long id2 = resultSet.getLong("id2");
        Prietenie prietenie = new Prietenie();
        prietenie.setId(new Tuplu<>(id1, id2));
        return prietenie;
    }

    @Override
    protected void setEntityParameters(PreparedStatement statement, Prietenie entity) throws SQLException {
        statement.setLong(1, entity.getId().getE1());
        statement.setLong(2, entity.getId().getE2());
    }

    @Override
    protected String getTableName() {
        return "Prietenii";
    }

    @Override
    protected String getPrimaryKeyCondition() {
        return "id1 = ? AND id2 = ?";
    }

    @Override
    protected void setPrimaryKeyParameters(PreparedStatement statement, Tuplu<Long, Long> id) throws SQLException {
        statement.setLong(1, id.getE1());
        statement.setLong(2, id.getE2());
    }

    @Override
    protected String getColumnNames() {
        return "id1, id2";
    }
}