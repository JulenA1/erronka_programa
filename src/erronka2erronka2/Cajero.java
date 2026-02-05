package erronka2erronka2;

import java.sql.*;

public class Cajero extends langileak {

    public Cajero(int Ln_id, String izena, String abizenak, String herrialdea, String email,
                  int posta_kodea, String erabiltzailea, String pasahitza, String rola,
                  Connection conn) {
        super(Ln_id, izena, abizenak, herrialdea, email, posta_kodea, erabiltzailea, pasahitza, rola, conn);
    }

    // Crear pedido para un cliente
    public int crearPedido(int Bz_id, float total) throws SQLException {
        String sql = "INSERT INTO pedidos (Bz_id, total_pedido) VALUES (?,?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, Bz_id);
        ps.setFloat(2, total);
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1); // devolver el id del pedido creado
        }
        return -1;
    }

    // Añadir productos al pedido
    public void añadirProductoAlPedido(int P_id, int Pr_id, String izena, int cantidad, float totalProducto) throws SQLException {
        String sql = "INSERT INTO pedido_productos (P_id, Pr_id, izena, kopurua, pr_kostu_totala) VALUES (?,?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, P_id);
        ps.setInt(2, Pr_id);
        ps.setString(3, izena);
        ps.setInt(4, cantidad);
        ps.setFloat(5, totalProducto);
        ps.executeUpdate();
    }
 // En la clase Cajero
    public void añadirLineaFactura(int fakturaId, String produktuIzena, double prezioUnitarioa, int kopurua) throws SQLException {
        double guztira = prezioUnitarioa * kopurua;
        String sql = "INSERT INTO fakturalerroa (fakturaId, produktuIzena, prezioUnitarioa, kopurua, guztira) VALUES (?,?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, fakturaId);
        ps.setString(2, produktuIzena);
        ps.setDouble(3, prezioUnitarioa);
        ps.setInt(4, kopurua);
        ps.setDouble(5, guztira);
        ps.executeUpdate();
    }

    // Crear factura para el pedido
    public void crearFactura(int P_id, int Bz_id, float total) throws SQLException {
        String sql = "INSERT INTO fakturak (P_id, Bz_id, fecha_faktura, total) VALUES (?,?,NOW(),?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, P_id);
        ps.setInt(2, Bz_id);
        ps.setFloat(3, total);
        ps.executeUpdate();
    }
}
