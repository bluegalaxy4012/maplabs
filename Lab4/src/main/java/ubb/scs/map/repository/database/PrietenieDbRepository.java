package ubb.scs.map.repository.database;

import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Tuplu;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.domain.validators.Validator;
import ubb.scs.map.utils.paging.Page;
import ubb.scs.map.utils.paging.Pageable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PrietenieDbRepository extends AbstractDbRepository<Tuplu<Long, Long>, Prietenie> {
    private final UtilizatorDbRepository utilizatorRepository;

    public PrietenieDbRepository(String url, String username, String password, Validator<Prietenie> validator, UtilizatorDbRepository utilizatorRepository) {
        super(url, username, password, validator);
        this.utilizatorRepository = utilizatorRepository;
    }

    public UtilizatorDbRepository getUtilizatorRepository() {
        return utilizatorRepository;
    }

    @Override
    protected Prietenie extractEntity(ResultSet resultSet) throws SQLException {
        Long id1 = resultSet.getLong("id1");
        Long id2 = resultSet.getLong("id2");
        LocalDateTime requestFrom = resultSet.getTimestamp("request_from").toLocalDateTime();
        Prietenie prietenie = new Prietenie();
        prietenie.setId(new Tuplu<>(id1, id2));
        prietenie.setRequestFrom(requestFrom);
        return prietenie;
    }

    @Override
    protected void setEntityParameters(PreparedStatement statement, Prietenie entity) throws SQLException {
        statement.setLong(1, entity.getId().getE1());
        statement.setLong(2, entity.getId().getE2());
        statement.setTimestamp(3, java.sql.Timestamp.valueOf(entity.getRequestFrom()));
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
        return "id1, id2, request_from";
    }

    @Override
    protected int getColumnCount() {
        return 3;
    }

    public Page<Utilizator> findAllOnPage(Pageable pageable, Long userId) {
        List<Utilizator> friends = new ArrayList<>();
        String query = "SELECT u.* FROM Utilizatori u JOIN Prietenii p1 ON u.id = p1.id2 JOIN Prietenii p2 ON p1.id1 = p2.id2 AND p1.id2 = p2.id1 WHERE p1.id1 = ? LIMIT ? OFFSET ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
            statement.setInt(2, pageable.getPageSize());
            statement.setInt(3, pageable.getPageSize() * pageable.getPageNumber());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                friends.add(utilizatorRepository.extractEntity(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int totalNumberOfElements = countFriends(userId);
        return new Page<>(friends, totalNumberOfElements);
    }

    private int countFriends(Long userId) {
        String query = "SELECT COUNT(*) FROM Utilizatori u JOIN Prietenii p1 ON u.id = p1.id2 JOIN Prietenii p2 ON p1.id1 = p2.id2 AND p1.id2 = p2.id1 WHERE p1.id1 = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}