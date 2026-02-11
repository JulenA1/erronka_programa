package erronka2erronka2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Soporte extends langileak {

    public Soporte(int Ln_id, String izena, String abizenak, String herrialdea, String email,
                    int posta_kodea, String erabiltzailea, String pasahitza, String rola,
                    Connection conn) {
        super(Ln_id, izena, abizenak, herrialdea, email, posta_kodea, erabiltzailea, pasahitza, rola, conn);
    }

    // FORMULARIOAK IKUSI
    public List<String> verFormularios() throws SQLException {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT * FROM formularioa";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            lista.add(rs.getInt("Fr_id") + " - " + rs.getString("izena") + " - " + rs.getString("email") + " - " + rs.getString("deskribapena"));
        }
        return lista;
    }
}
