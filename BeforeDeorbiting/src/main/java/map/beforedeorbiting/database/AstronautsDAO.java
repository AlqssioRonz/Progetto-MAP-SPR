package map.beforedeorbiting.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe per l'accesso ai dati nel database.
 * <p>
 * Fornisce metodi per creare la tabella {@code astronauts}, inserire nuovi
 * record e recuperare tutti gli astronauti.
 * </p>
 *
 * @author lorenzopeluso
 */
public class AstronautsDAO {

    /**
     * Connessione JDBC utilizzata per comunicare con il database.
     */
    private final Connection conn;

    /**
     * Costruisce un DAO per gli astronauti a partire da una connessione JDBC.
     *
     * @param conn la connessione al database
     */
    public AstronautsDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Crea la tabella {@code astronauts} se non esiste già.
     *
     * @throws SQLException in caso di errori durante l'esecuzione dello
     * statement SQL
     */
    public void createTable() throws SQLException {
        String sql = """
                    CREATE TABLE IF NOT EXISTS astronauts (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(100),
                        surname VARCHAR(100),
                        date_of_birth DATE,
                        birthplace VARCHAR(200),
                        time_on_iss_hours INT
                    );
                """;
        conn.createStatement().execute(sql);
    }

    /**
     * Inserisce un nuovo astronauta nel database.
     *
     * @param a l'astronauta da inserire (nome, cognome, data di nascita, luogo
     * di nascita, ore in ISS)
     * @throws SQLException in caso di errori durante l'inserimento
     */
    public void insert(Astronaut a) throws SQLException {
        String sql = "INSERT INTO astronauts (name, surname, date_of_birth, birthplace, time_on_iss_hours) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, a.getName());
            statement.setString(2, a.getSurname());
            statement.setDate(3, Date.valueOf(a.getDateOfBirth()));
            statement.setString(4, a.getBirthplace());
            statement.setInt(5, a.getHoursOnISS());
            statement.executeUpdate();
        }
    }

    /**
     * Recupera tutti gli astronauti memorizzati nel database.
     *
     * @return lista di {@link Astronaut} presenti nella tabella
     * @throws SQLException in caso di errori durante l'esecuzione della query
     */
    public List<Astronaut> getAll() throws SQLException {
        List<Astronaut> list = new ArrayList<>();
        String sql = "SELECT * FROM astronauts";
        try (ResultSet rs = conn.createStatement().executeQuery(sql)) {
            while (rs.next()) {
                Astronaut a = new Astronaut();
                a.setName(rs.getString("name"));
                a.setSurname(rs.getString("surname"));
                a.setDateOfBirth(rs.getDate("date_of_birth").toString());
                a.setBirthplace(rs.getString("birthplace"));
                a.setHoursOnISS(rs.getInt("time_on_iss_hours"));
                list.add(a);
            }
        }
        return list;
    }
}
