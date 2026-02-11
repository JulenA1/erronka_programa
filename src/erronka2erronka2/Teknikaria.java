package erronka2erronka2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Teknikaria extends langileak {

    public Teknikaria(int id, String izena, String abizenak, String herrialdea, String email,
                       int posta_kodea, String erabiltzailea, String pasahitza, String rola,
                       Connection conn) {
        super(id, izena, abizenak, herrialdea, email, posta_kodea, erabiltzailea, pasahitza, rola, conn);
    }

    //ESTROPEATUTAKO/AZTERTZEKO PRODUKTUAK IKUSI
    public List<String> verProductosParaReparar() throws SQLException {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT * FROM produktuak WHERE egoera='estropeatuta' OR egoera='aztertzeko'";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            lista.add(
                "ID: " + rs.getInt("Pr_id") +
                " | Izena: " + rs.getString("izena") +
                " | Deskribapena: " + rs.getString("deskripzioa") +
                " | Prezioa: " + rs.getDouble("prezioa") +
                " | Egoera: " + rs.getString("egoera") +
                " | Stock: " + rs.getInt("stock") +
                " | Irudia: " + rs.getString("irudia") +
                " | ID Hornitzaile: " + rs.getObject("hornitzaile_id")
            );
        }

        return lista;
    }



    // PRODUKTU EGOERA ALDATU
    public void repararProducto(int Pr_id) throws SQLException {
        String sql = "UPDATE produktuak SET egoera='konponduta' WHERE Pr_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, Pr_id);
        ps.executeUpdate();
    }
}
