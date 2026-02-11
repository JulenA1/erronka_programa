package erronka2erronka2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

public class Biltegia extends langileak {

    public Biltegia(int Ln_id, String izena, String abizenak, String herrialdea, String email,
                    int posta_kodea, String erabiltzailea, String pasahitza, String rola,
                    Connection conn) {
        super(Ln_id, izena, abizenak, herrialdea, email, posta_kodea, erabiltzailea, pasahitza, rola, conn);
    }

    //PRODUKTUAK IKUSI
    public List<String> verProductos() throws SQLException {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT * FROM produktuak WHERE egoera='konponduta'";
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
                " | Hornitzaile ID: " + rs.getInt("hornitzaile_id")
            );
        }
        return lista;
    }
    //PRODUKTUAK SARTU
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
    //PRODUKTUAK AKTUALIZATU
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
    //PRODUKTUAK BORRATU
    public void borrarProducto(int Pr_id) throws SQLException {
        String sql = "DELETE FROM produktuak WHERE Pr_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, Pr_id);
        ps.executeUpdate();
    }

    //ESKAERAK IKUSI
    public List<String> verPedidos() throws SQLException {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedidos";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            lista.add(
                rs.getInt("P_id") +
                " - Bezeroa: " + rs.getInt("Bz_id") +
                " - Total: " + rs.getFloat("total_pedido") +
                " - Egoera: " + rs.getString("estado_entrega")
            );
        }
        return lista;
    }
    //ESKAERA EGOERA ONARTU ETA PDF SORTU
    public void aceptarPedido(int P_id) throws SQLException {

        String sqlSelect = "SELECT * FROM pedidos p " +
                           "JOIN pedido_productos pp ON p.P_id = pp.P_id " +
                           "WHERE p.P_id=?";

        PreparedStatement psSelect = conn.prepareStatement(sqlSelect);
        psSelect.setInt(1, P_id);
        ResultSet rs = psSelect.executeQuery();

        if (!rs.next()) {
            System.out.println("Pedido no encontrado");
            return;
        }

        int clienteId = rs.getInt("Bz_id");
        String fecha = rs.getString("fecha");
        double total = rs.getDouble("total_pedido");

        List<String[]> productos = new ArrayList<>();

        do {
            String nombre = rs.getString("izena");
            int cantidad = rs.getInt("kopurua");
            double precio = rs.getDouble("pr_kostu_totala");

            productos.add(new String[]{nombre, String.valueOf(cantidad), String.valueOf(precio)});
        } while (rs.next());

        String rutaLocal = "C:/xampp/htdocs/weberronka/fakturak/faktura_" + P_id + ".pdf";

        try {
            PdfWriter writer = new PdfWriter(rutaLocal);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Eskaera ticket id: " + P_id));
            document.add(new Paragraph("Bezero ID: " + clienteId));
            document.add(new Paragraph("Data: " + fecha));
            document.add(new Paragraph("Egoera: enviado"));
            document.add(new Paragraph(" "));

            float[] columnas = {200F, 100F, 100F};
            Table table = new Table(columnas);

            table.addCell("Producto");
            table.addCell("Cantidad");
            table.addCell("Precio");

            for (String[] prod : productos) {
                table.addCell(prod[0]);
                table.addCell(prod[1]);
                table.addCell(prod[2]);
            }

            document.add(table);
            document.add(new Paragraph("Total: " + total));
            document.close();


            String rutaRemota = "/faktura_" + P_id + ".pdf";
            subirFTP(rutaLocal, rutaRemota);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // ========= UPDATE DB =========
        String sqlUpdate = "UPDATE pedidos SET estado_entrega='enviado', faktura=? WHERE P_id=?";
        PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);

        psUpdate.setString(1, "fakturak/faktura_" + P_id + ".pdf");
        psUpdate.setInt(2, P_id);
        psUpdate.executeUpdate();

        System.out.println("Eskaera onartuta");
    }

    // PDF ZERBITZARIRA IGO
    private void subirFTP(String archivoLocal, String archivoRemoto) {

        FTPClient ftp = new FTPClient();

        try {
            ftp.connect("192.168.115.67", 21);
            if (!ftp.login("Julen", "Julen12*")) {
                System.out.println("Login FTP fallido");
                return;
            }

            ftp.enterLocalPassiveMode();
            ftp.setFileType(FTP.BINARY_FILE_TYPE);

            FileInputStream fis = new FileInputStream(new File(archivoLocal));
            boolean ok = ftp.storeFile(archivoRemoto, fis);
            fis.close();

            if (ok)
                System.out.println("PDF-a zuzen igo da zerbitzarira");
            else
                System.out.println("Error al subir PDF.");

            ftp.logout();
            ftp.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
