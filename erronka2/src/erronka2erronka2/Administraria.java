package erronka2erronka2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Administraria extends langileak {

    public Administraria(int Ln_id, String izena, String abizenak, String herrialdea, String email,
                         int posta_kodea, String erabiltzailea, String pasahitza, String rola,
                         Connection conn) {
        super(Ln_id, izena, abizenak, herrialdea, email, posta_kodea, erabiltzailea, pasahitza, rola, conn);
    }

    // BEZEROAK ZERRENDA IKUSI
    public List<String> verBezeroak() throws SQLException {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT * FROM bezeroak";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            lista.add(
                "ID:" + rs.getInt("Bz_id") +
                " | Izena: " + rs.getString("izena") +
                " | Abizenak: " + rs.getString("abizenak") +
                " | NAN: " + rs.getString("Nan") +
                " | Helbidea: " + rs.getString("helbidea") +
                " | Email: " + rs.getString("email") +
                " | erabiltzailea: " + rs.getString("erabiltzailea") +
                " | Rol: " + rs.getString("rol")
            );
        }
        return lista;
    }

    //BEZEROAK SARTU
    public void añadirBezero(String izena, String abizenak, String Nan, String helbidea,
                             String email, String erabiltzailea, String pasahitza, String rol) throws SQLException {
        String sql = "INSERT INTO bezeroak (izena, abizenak, Nan, helbidea, email, erabiltzailea, pasahitza, rol) VALUES (?,?,?,?,?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, izena);
        ps.setString(2, abizenak);
        ps.setString(3, Nan);
        ps.setString(4, helbidea);
        ps.setString(5, email);
        ps.setString(6, erabiltzailea);
        ps.setString(7, pasahitza);
        ps.setString(8, rol);
        ps.executeUpdate();
    }
    //BEZEROAK EZABATU
    public void borrarBezero(int Bz_id) throws SQLException {
        String sql = "DELETE FROM bezeroak WHERE Bz_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, Bz_id);
        ps.executeUpdate();
    }
    //BEZEROAK AKTUALIZATU
    public void actualizarBezero(int Bz_id, String izena, String abizenak, String email) throws SQLException {
        String sql = "UPDATE bezeroak SET izena=?, abizenak=?, email=? WHERE Bz_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, izena);
        ps.setString(2, abizenak);
        ps.setString(3, email);
        ps.setInt(4, Bz_id);
        ps.executeUpdate();
    }

    //LANGILEAK ZERRENDA IKUSI
    public List<String> verLangileak() throws SQLException {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT * FROM langileak";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            lista.add(
                "ID:" + rs.getInt("Ln_id") +
                " | Izena: " + rs.getString("izena") +
                " | Abizenak: " + rs.getString("abizenak") +
                " | Herrialdea: " + rs.getString("herrialdea") +
                " | Email: " + rs.getString("email") +
                " | Posta kodea: " + rs.getInt("posta_kodea") +
                " | Erabiltzailea: " + rs.getString("erabiltzailea") +
                " | Pasahitza: " + rs.getString("pasahitza") +
                " | Rol: " + rs.getString("rola")
            );
        }
        return lista;
    }

    //LANGILEA SARTU
    public void añadirLangile(String izena, String abizenak, String herrialdea, String email,
                              int posta_kodea, String erabiltzailea, String pasahitza, String rola) throws SQLException {
        String sql = "INSERT INTO langileak (izena, abizenak, herrialdea, email, posta_kodea, erabiltzailea, pasahitza, rola) VALUES (?,?,?,?,?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, izena);
        ps.setString(2, abizenak);
        ps.setString(3, herrialdea);
        ps.setString(4, email);
        ps.setInt(5, posta_kodea);
        ps.setString(6, erabiltzailea);
        ps.setString(7, pasahitza);
        ps.setString(8, rola);
        ps.executeUpdate();
    }
    //LANGILEA EZABATU
    public void borrarLangile(int Ln_id) throws SQLException {
        String sql = "DELETE FROM langileak WHERE Ln_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, Ln_id);
        ps.executeUpdate();
    }
    //LANGILEA AKTUALIZATU
    public void actualizarLangile(int Ln_id, String izena, String abizenak, String email) throws SQLException {
        String sql = "UPDATE langileak SET izena=?, abizenak=?, email=? WHERE Ln_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, izena);
        ps.setString(2, abizenak);
        ps.setString(3, email);
        ps.setInt(4, Ln_id);
        ps.executeUpdate();
    }
}
