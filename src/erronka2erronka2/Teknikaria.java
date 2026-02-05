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

    // Ver productos estropeados o por analizar
    public List<String> verProductosParaReparar() throws SQLException {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT * FROM produktuak WHERE egoera='estropeatuta' OR egoera='aztertzeko'";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            lista.add(
                "ID: " + rs.getInt("Pr_id") +
                " | Nombre: " + rs.getString("izena") +
                " | Descripción: " + rs.getString("deskripzioa") +
                " | Precio: " + rs.getDouble("prezioa") +
                " | Estado: " + rs.getString("egoera") +
                " | Stock: " + rs.getInt("stock") +
                " | Imagen: " + rs.getString("irudia") +
                " | ID Proveedor: " + rs.getObject("hornitzaile_id")  // <-- nombre exacto de la columna
            );
        }

        return lista;
    }



    // Cambiar estado del producto a 'konponduta'
    public void repararProducto(int Pr_id) throws SQLException {
        String sql = "UPDATE produktuak SET egoera='konponduta' WHERE Pr_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, Pr_id);
        ps.executeUpdate();
    }
}
