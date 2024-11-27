package ubb.scs.map.repository.database;

import ubb.scs.map.domain.Mesaj;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.domain.validators.Validator;
import ubb.scs.map.repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MesajDbRepository extends AbstractDbRepository<Long, Mesaj> {

    private final Repository<Long, Utilizator> utilizatorRepository;

    public MesajDbRepository(String url, String username, String password, Validator<Mesaj> validator, Repository<Long, Utilizator> utilizatorRepository) {
        super(url, username, password, validator);
        this.utilizatorRepository = utilizatorRepository;
    }

    @Override
    protected Mesaj extractEntity(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        Long fromId = resultSet.getLong("from_id");
        String messageText = resultSet.getString("text");
        LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
        Long repliedMesajId = resultSet.getLong("replied_mesaj_id");

        Utilizator from = utilizatorRepository.findOne(fromId).orElseThrow(() -> new SQLException("Utilizator expeditor negasit")); //desi nu ar trebui
        List<Utilizator> to = getRecipientsByMessageId(id);
        Mesaj repliedMesaj = repliedMesajId != 0 ? findOne(repliedMesajId).orElse(null) : null;

        Mesaj mesaj = new Mesaj(from, to, messageText);
        mesaj.setId(id);
        mesaj.setDate(date);
        mesaj.setRepliedMessage(repliedMesaj);
        return mesaj;
    }

    @Override
    protected void setEntityParameters(PreparedStatement statement, Mesaj entity) throws SQLException {
        statement.setLong(1, entity.getFrom().getId());
        statement.setString(2, entity.getText());
        statement.setTimestamp(3, Timestamp.valueOf(entity.getDate()));
        if (entity.getRepliedMessage() != null) {
            statement.setLong(4, entity.getRepliedMessage().getId());
        } else {
            statement.setNull(4, Types.BIGINT);
        }
    }

    @Override
    protected String getTableName() {
        return "Mesaje";
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
        return "from_id, text, date, replied_mesaj_id";
    }

    @Override
    protected int getColumnCount() {
        return 4;
    }

    @Override
    public Optional<Mesaj> save(Mesaj mesaj) {
        validator.validate(mesaj);
        Optional<Mesaj> savedMesaj = super.save(mesaj);

        if (savedMesaj.isEmpty()) {
            try {
                String query = "SELECT currval(pg_get_serial_sequence('Mesaje', 'id'))";
                try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                    if (rs.next()) {
                        mesaj.setId(rs.getLong(1));
                    }
                }
                saveRecipients(mesaj);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return savedMesaj;
    }

    private void saveRecipients(Mesaj mesaj) throws SQLException {
        String query = "INSERT INTO RecipientiMesaje (mesaj_id, to_id) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (Utilizator recipient : mesaj.getTo()) {
                statement.setLong(1, mesaj.getId());
                statement.setLong(2, recipient.getId());
                statement.executeUpdate();
            }
        }
    }

    private List<Utilizator> getRecipientsByMessageId(Long messageId) throws SQLException {
        List<Utilizator> recipients = new ArrayList<>();
        String query = "SELECT to_id FROM RecipientiMesaje WHERE mesaj_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, messageId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long toId = resultSet.getLong("to_id");
                Utilizator recipient = utilizatorRepository.findOne(toId).orElseThrow(() -> new SQLException("Utilizator destinatar negasit")); //desi nu ar trebui
                recipients.add(recipient);
            }
        }
        return recipients;
    }
}