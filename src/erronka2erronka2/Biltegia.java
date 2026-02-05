package erronka2erronka2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Biltegia extends langileak {

    public Biltegia(int Ln_id, String izena, String abizenak, String herrialdea, String email,
                     int posta_kodea, String erabiltzailea, String pasahitza, String rola,
                     Connection conn) {
        super(Ln_id, izena, abizenak, herrialdea, email, posta_kodea, erabiltzailea, pasahitza, rola, conn);
    }

 // Ver productos en estado 'konponduta' mostrando todos los campos
    public List<String> verProductos() throws SQLException {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT * FROM produktuak WHERE egoera='konponduta'";
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
                " | Hornitzaile ID: " + rs.getInt("hornitzaile_id")
            );
        }
        return lista;
    }


    // Gestionar pedidos: ver todos
    public List<String> verPedidos() throws SQLException {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedidos";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            lista.add(rs.getInt("P_id") + " - Cliente: " + rs.getInt("Bz_id") + " - Total: " + rs.getFloat("total_pedido") + " - Estado: " + rs.getString("estado_entrega"));
        }
        return lista;
    }

    // Aceptar pedido: cambiar estado a 'enviado'
    public void aceptarPedido(int P_id) throws SQLException {
        String sql = "UPDATE pedidos SET estado_entrega='enviado' WHERE P_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, P_id);
        ps.executeUpdate();
    }

    // Añadir producto nuevo
    public void añadirProducto(String izena, String deskripzioa, float precioa, int stock, String egoera) throws SQLException {
        String sql = "INSERT INTO produktuak (izena, deskripzioa, prezioa, stock, egoera) VALUES (?,?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, izena);
        ps.setString(2, deskripzioa);
        ps.setFloat(3, precioa);
        ps.setInt(4, stock);
        ps.setString(5, egoera);
        ps.executeUpdate();
    }

    // Modificar producto existente
    public void actualizarProducto(int Pr_id, String izena, String deskripzioa, float precioa, int stock) throws SQLException {
        String sql = "UPDATE produktuak SET izena=?, deskripzioa=?, prezioa=?, stock=? WHERE Pr_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, izena);
        ps.setString(2, deskripzioa);
        ps.setFloat(3, precioa);
        ps.setInt(4, stock);
        ps.setInt(5, Pr_id);
        ps.executeUpdate();
    }

    // Eliminar producto
    public void borrarProducto(int Pr_id) throws SQLException {
        String sql = "DELETE FROM produktuak WHERE Pr_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, Pr_id);
        ps.executeUpdate();
    }
}
