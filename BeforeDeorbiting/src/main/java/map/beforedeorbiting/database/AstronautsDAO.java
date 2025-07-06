/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Classe per l'accesso ai dati del db
 * 
 * @author lorenzopeluso
 */
public class AstronautsDAO {
    private final Connection conn;

    public AstronautsDAO(Connection conn) {
        this.conn = conn;
    }

    public void createTable() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS astronauts (
                id INT AUTO INCREMENT PRIMARY KEY,
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
     * 
     * @param a
     * @throws SQLException 
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
     * 
     * @return
     * @throws SQLException 
     */
    public List<Astronaut> getAll() throws SQLException {
        List<Astronaut> list = new ArrayList<>();
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM astronauts");
        while (rs.next()) {
            Astronaut a = new Astronaut();
            a.setName(rs.getString("name"));
            a.setSurname(rs.getString("surname"));
            a.setDateOfBirth(rs.getDate("date_of_birth").toString());
            a.setBirthplace(rs.getString("birthplace"));
            a.setHoursOnISS(rs.getInt("time_on_iss_hours"));
            list.add(a);
        }
        return list;
    }
}
