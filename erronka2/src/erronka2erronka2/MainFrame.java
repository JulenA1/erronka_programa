package erronka2erronka2;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class MainFrame extends JFrame {

    private Connection conn;

    private JTextField tfUsuario;
    private JPasswordField pfPassword;
    private JButton btnLogin;
    private JLabel lblMensaje;

    private JPanel loginPanel;
    private JPanel menuPanel;

    private langileak usuarioActual;

    public MainFrame(Connection conn) {
        this.conn = conn;
        setTitle("Gestio sistema - ReTech");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initLoginPanel();
        add(loginPanel);

        setVisible(true);
    }

    private void initLoginPanel() {
        loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());

        JLabel lblUsuario = new JLabel("Erabiltzailea:");
        JLabel lblPassword = new JLabel("Pasahitza:");
        tfUsuario = new JTextField(15);
        pfPassword = new JPasswordField(15);
        btnLogin = new JButton("Login");
        lblMensaje = new JLabel("");

        Insets insets = new Insets(5, 5, 5, 5);

        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.insets = insets;
        gbc1.gridx = 0; gbc1.gridy = 0;
        loginPanel.add(lblUsuario, gbc1);

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = insets;
        gbc2.gridx = 1; gbc2.gridy = 0;
        loginPanel.add(tfUsuario, gbc2);

        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.insets = insets;
        gbc3.gridx = 0; gbc3.gridy = 1;
        loginPanel.add(lblPassword, gbc3);

        GridBagConstraints gbc4 = new GridBagConstraints();
        gbc4.insets = insets;
        gbc4.gridx = 1; gbc4.gridy = 1;
        loginPanel.add(pfPassword, gbc4);

        GridBagConstraints gbc5 = new GridBagConstraints();
        gbc5.insets = insets;
        gbc5.gridx = 0; gbc5.gridy = 2; gbc5.gridwidth = 2;
        loginPanel.add(btnLogin, gbc5);

        GridBagConstraints gbc6 = new GridBagConstraints();
        gbc6.insets = insets;
        gbc6.gridx = 0; gbc6.gridy = 3; gbc6.gridwidth = 2;
        loginPanel.add(lblMensaje, gbc6);

        btnLogin.addActionListener(e -> hacerLogin());
    }

    private void hacerLogin() {
        String usuario = tfUsuario.getText();
        String password = new String(pfPassword.getPassword());

        try {
            String sql = "SELECT * FROM langileak WHERE erabiltzailea=? AND pasahitza=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, usuario);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("Ln_id");
                String izena = rs.getString("izena");
                String abizenak = rs.getString("abizenak");
                String herrialdea = rs.getString("herrialdea");
                String email = rs.getString("email");
                int posta_kodea = rs.getInt("posta_kodea");
                String pasahitza = rs.getString("pasahitza");
                String erabiltzailea = rs.getString("erabiltzailea");
                String rol = rs.getString("rola");

                switch (rol) {
                    case "Administraria":
                        usuarioActual = new Administraria(id, izena, abizenak, herrialdea, email, posta_kodea,
                                erabiltzailea, pasahitza, rol, conn);
                        break;
                    case "Teknikaria":
                        usuarioActual = new Teknikaria(id, izena, abizenak, herrialdea, email, posta_kodea,
                                erabiltzailea, pasahitza, rol, conn);
                        break;
                    case "Biltegia":
                        usuarioActual = new Biltegia(id, izena, abizenak, herrialdea, email, posta_kodea,
                                erabiltzailea, pasahitza, rol, conn);
                        break;
                    case "Soporte":
                        usuarioActual = new Soporte(id, izena, abizenak, herrialdea, email, posta_kodea,
                                erabiltzailea, pasahitza, rol, conn);
                        break;
                    default:
                        lblMensaje.setText("Rol ez detektatuta");
                        return;
                }

                mostrarMenu(rol);
            } else {
                lblMensaje.setText("Erabiltzaile edo pasahitza okerrak.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            lblMensaje.setText("Error datu basera sartzeko");
        }
    }

    private void mostrarMenu(String rol) {
        remove(loginPanel);

        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        JLabel lblBienvenida = new JLabel("Kaixo, " + usuarioActual.izena + " (" + rol + ")");
        lblBienvenida.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuPanel.add(lblBienvenida);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        switch (rol) {
            case "Administraria":
                JButton btnVerBezeroak = new JButton("Bezeroak administratu");
                JButton btnVerLangileak = new JButton("Langileak administratu");
                btnVerBezeroak.setAlignmentX(Component.CENTER_ALIGNMENT);
                btnVerLangileak.setAlignmentX(Component.CENTER_ALIGNMENT);
                menuPanel.add(btnVerBezeroak);
                menuPanel.add(btnVerLangileak);

                btnVerBezeroak.addActionListener(e -> gestionarBezeroak());
                btnVerLangileak.addActionListener(e -> gestionarLangileak());
                break;

            case "Teknikaria":
                JButton btnVerProductos = new JButton("Konpondu beharreko produktuak ikusi");
                btnVerProductos.setAlignmentX(Component.CENTER_ALIGNMENT);
                menuPanel.add(btnVerProductos);

                btnVerProductos.addActionListener(e -> verProductosTeknikaria());
                break;

            case "Biltegia":
                JButton btnVerProductosB = new JButton("Produktuak ikusi");
                JButton btnVerPedidos = new JButton("Eskaerak ikusi");
                JButton btnAceptarPedido = new JButton("Eskaera onartu");
                btnVerProductosB.setAlignmentX(Component.CENTER_ALIGNMENT);
                btnVerPedidos.setAlignmentX(Component.CENTER_ALIGNMENT);
                btnAceptarPedido.setAlignmentX(Component.CENTER_ALIGNMENT);
                menuPanel.add(btnVerProductosB);
                menuPanel.add(btnVerPedidos);
                menuPanel.add(btnAceptarPedido);

                btnVerProductosB.addActionListener(e -> gestionarProductosBiltegia());
                btnVerPedidos.addActionListener(e -> verPedidosBiltegia());

                btnAceptarPedido.addActionListener(e -> {
                    try {
                        Biltegia b = (Biltegia) usuarioActual;
                        List<String> lista = b.verPedidos();
                        StringBuilder sb = new StringBuilder();
                        for (String s : lista) sb.append(s).append("\n");

                        String idStr = JOptionPane.showInputDialog(this, sb + "\n Onartu nahi den eskaeraren id:");
                        if (idStr != null && !idStr.isEmpty()) {
                            int id = Integer.parseInt(idStr);
                            b.aceptarPedido(id);
                            JOptionPane.showMessageDialog(this, "Eskaera onartuta eta ticket-a sortuta.");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Error eskaera onartzearekin.");
                    }
                });

                break;

            case "Soporte":
                JButton btnVerFormularios = new JButton("Formularioak ikusi");
                btnVerFormularios.setAlignmentX(Component.CENTER_ALIGNMENT);
                menuPanel.add(btnVerFormularios);

                btnVerFormularios.addActionListener(e -> verFormulariosSoporte());
                break;
        }

        JButton btnCerrarSesion = new JButton("Sesioa itxi");
        btnCerrarSesion.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        menuPanel.add(btnCerrarSesion);

        btnCerrarSesion.addActionListener(e -> cerrarSesion());

        add(menuPanel);
        revalidate();
        repaint();
    }

    private void cerrarSesion() {
        remove(menuPanel);
        tfUsuario.setText("");
        pfPassword.setText("");
        lblMensaje.setText("");
        add(loginPanel);
        revalidate();
        repaint();
    }

    // ADMINISTRARIA METODOAK
    private void gestionarBezeroak() {
        try {
            Administraria admin = (Administraria) usuarioActual;
            List<String> lista = admin.verBezeroak();
            StringBuilder sb = new StringBuilder();
            for (String b : lista) sb.append(b).append("\n");

            int op = JOptionPane.showOptionDialog(
                    this,
                    sb.toString(),
                    "Bezeroak",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new String[]{"Sortu", "Ezabatu", "Aktualizatu", "Cancelar"},
                    "Cancelar"
            );

            if (op == 0) {
                String nombre = JOptionPane.showInputDialog(this, "Izena:");
                String apellidos = JOptionPane.showInputDialog(this, "Abizenak:");
                String Nan = JOptionPane.showInputDialog(this, "NAN:");
                String direccion = JOptionPane.showInputDialog(this, "Helbidea:");
                String email = JOptionPane.showInputDialog(this, "Email:");
                String usuario = JOptionPane.showInputDialog(this, "Erabiltzailea:");
                String password = JOptionPane.showInputDialog(this, "Pasahitza:");
                String rol = JOptionPane.showInputDialog(this, "Rol:");
                admin.añadirBezero(nombre, apellidos, Nan, direccion, email, usuario, password, rol);
            }

            if (op == 1) {
                String id = JOptionPane.showInputDialog(this, sb + "\nEzabatu nahi den bezeroaren id:");
                if (id != null && !id.isEmpty()) {
                    admin.borrarBezero(Integer.parseInt(id));
                    JOptionPane.showMessageDialog(this, "Bezeroa zuzen ezabatu da.");
                }
            }

            if (op == 2) {
                actualizarBezeroAdmin(lista);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void actualizarBezeroAdmin(List<String> lista) {
        try {
            StringBuilder sb = new StringBuilder();
            for (String b : lista) sb.append(b).append("\n");

            String idStr = JOptionPane.showInputDialog(this, sb + "\nAktualizatu beharreko bezeroaren id:");
            if (idStr == null || idStr.isEmpty()) return;
            int id = Integer.parseInt(idStr);

            String[] campos = lista.stream()
                    .filter(s -> s.contains("ID:" + id))
                    .findFirst()
                    .orElse("")
                    .split("\\|");

            String nombre = JOptionPane.showInputDialog(this, "Izena:", campos.length > 1 ? campos[1].split(":")[1].trim() : "");
            String apellidos = JOptionPane.showInputDialog(this, "Abizenak:", campos.length > 2 ? campos[2].split(":")[1].trim() : "");
            String email = JOptionPane.showInputDialog(this, "Email:", campos.length > 5 ? campos[5].split(":")[1].trim() : "");

            ((Administraria) usuarioActual).actualizarBezero(id, nombre, apellidos, email);
            JOptionPane.showMessageDialog(this, "Bezeroa zuzen aktualizatuta");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void gestionarLangileak() {
        try {
            Administraria admin = (Administraria) usuarioActual;
            List<String> lista = admin.verLangileak();
            StringBuilder sb = new StringBuilder();
            for (String l : lista) sb.append(l).append("\n");

            int op = JOptionPane.showOptionDialog(
                    this,
                    sb.toString(),
                    "Langileak",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new String[]{"Sartu", "Ezabatu", "Aktualizatu", "Cancelar"},
                    "Cancelar"
            );

            if (op == 0) {
                String nombre = JOptionPane.showInputDialog(this, "Izena:");
                String apellidos = JOptionPane.showInputDialog(this, "Abizenak:");
                String pais = JOptionPane.showInputDialog(this, "Herrialdea:");
                String email = JOptionPane.showInputDialog(this, "Email:");
                int codigoPostal = Integer.parseInt(JOptionPane.showInputDialog(this, "Posta kodea:"));
                String usuario = JOptionPane.showInputDialog(this, "Erabiltzailea:");
                String password = JOptionPane.showInputDialog(this, "Pasahitza:");
                String rol = JOptionPane.showInputDialog(this, "Rol:");
                admin.añadirLangile(nombre, apellidos, pais, email, codigoPostal, usuario, password, rol);
            }

            if (op == 1) {
                String id = JOptionPane.showInputDialog(this, sb + "\nBorratu nahi den langilearen id:");
                if (id != null && !id.isEmpty()) {
                    admin.borrarLangile(Integer.parseInt(id));
                    JOptionPane.showMessageDialog(this, "Langilea zuzen borratuta");
                }
            }

            if (op == 2) {
                actualizarLangileAdmin(lista);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void actualizarLangileAdmin(List<String> lista) {
        try {
            // Mostrar la lista completa de langileak primero
            StringBuilder sb = new StringBuilder();
            for (String l : lista) sb.append(l).append("\n");

            // Pedir el ID del langilea a actualizar
            String idStr = JOptionPane.showInputDialog(this, sb + "\nAktualizatu beharreko langilearen id:");
            if (idStr == null || idStr.isEmpty()) return;
            int id = Integer.parseInt(idStr);

            // Buscar los datos actuales del langilea en la lista
            String[] campos = lista.stream()
                    .filter(s -> s.contains("ID:" + id))
                    .findFirst()
                    .orElse("")
                    .split("\\|");

            String nombre = JOptionPane.showInputDialog(this, "Izena:", campos.length > 1 ? campos[1].split(":")[1].trim() : "");
            String apellidos = JOptionPane.showInputDialog(this, "Abizenak:", campos.length > 2 ? campos[2].split(":")[1].trim() : "");
            String email = JOptionPane.showInputDialog(this, "Email:", campos.length > 4 ? campos[4].split(":")[1].trim() : "");

            ((Administraria) usuarioActual).actualizarLangile(id, nombre, apellidos, email);
            JOptionPane.showMessageDialog(this, "Langilea zuzen aktualizatuta");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // TEKNIKARIA METODOAK
    private void verProductosTeknikaria() {
        try {
            Teknikaria teknikaria = (Teknikaria) usuarioActual;
            List<String> lista = teknikaria.verProductosParaReparar();
            StringBuilder sb = new StringBuilder();
            for (String p : lista) sb.append(p).append("\n");
            JOptionPane.showMessageDialog(this, sb.toString(), "Konpontzeko produktuak", JOptionPane.INFORMATION_MESSAGE);

            String idStr = JOptionPane.showInputDialog(this, sb.toString() + "\nKonpondu beharreko produktuaren id-a:");
            if (idStr != null && !idStr.isEmpty()) {
                int id = Integer.parseInt(idStr);
                teknikaria.repararProducto(id);
                JOptionPane.showMessageDialog(this, "Produktua konponduta bezala ezarrita.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // BILTEGIA METODOAK
    private void gestionarProductosBiltegia() {
        try {
            Biltegia b = (Biltegia) usuarioActual;
            List<String> lista = b.verProductos();

            StringBuilder sb = new StringBuilder();
            for (String s : lista) sb.append(s).append("\n");

            int op = JOptionPane.showOptionDialog(this, sb, "Productos",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new String[]{"Sartu", "Ezabatu", "Aktualizatu", "Cancelar"},
                    "Cancelar");

            if (op == 0) {
                String n = JOptionPane.showInputDialog("Izena:");
                String d = JOptionPane.showInputDialog("Deskripzioa:");
                float p = Float.parseFloat(JOptionPane.showInputDialog("Prezioa:"));
                int s = Integer.parseInt(JOptionPane.showInputDialog("Stock:"));
                String e = JOptionPane.showInputDialog("Egoera:");
                b.añadirProducto(n, d, p, s, e);
            }

            if (op == 1) {
                String id = JOptionPane.showInputDialog(sb + "\nEzabatzeko produktuaren id-a:");
                if (id != null) b.borrarProducto(Integer.parseInt(id));
            }

            if (op == 2) {
                actualizarProductoBiltegia();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void actualizarProductoBiltegia() {
        try {
            Biltegia b = (Biltegia) usuarioActual;
            List<String> lista = b.verProductos();

            StringBuilder sb = new StringBuilder();
            for (String s : lista) sb.append(s).append("\n");

            String idStr = JOptionPane.showInputDialog(this, sb + "\nAktualizatzeko produktuaren id-a:");
            if (idStr == null) return;
            int id = Integer.parseInt(idStr);

            String[] campos = lista.stream()
                    .filter(x -> x.contains("ID: " + id))
                    .findFirst().orElse("").split("\\|");

            String nombre = JOptionPane.showInputDialog("Izena:", campos[1].split(":")[1].trim());
            String desc = JOptionPane.showInputDialog("Deskripzioa:", campos[2].split(":")[1].trim());
            float precio = Float.parseFloat(JOptionPane.showInputDialog("Prezioa:", campos[3].split(":")[1].trim()));
            int stock = Integer.parseInt(JOptionPane.showInputDialog("Stock:", campos[5].split(":")[1].trim()));
            String img = JOptionPane.showInputDialog("Irudia:", campos[6].split(":")[1].trim());

            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE produktuak SET izena=?, deskripzioa=?, prezioa=?, stock=?, irudia=? WHERE Pr_id=?"
            );
            ps.setString(1, nombre);
            ps.setString(2, desc);
            ps.setFloat(3, precio);
            ps.setInt(4, stock);
            ps.setString(5, img);
            ps.setInt(6, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void verPedidosBiltegia() {
        try {
            Biltegia b = (Biltegia) usuarioActual;
            List<String> lista = b.verPedidos();
            StringBuilder sb = new StringBuilder();
            for (String s : lista) sb.append(s).append("\n");
            JOptionPane.showMessageDialog(this, sb.toString(), "Pedidos", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

 // SOPORTE METODOAK
    private void verFormulariosSoporte() {
        try {
            Soporte soporte = (Soporte) usuarioActual;
            List<String> lista = soporte.verFormularios();

            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ez daude formulariorik.", "Formularios", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder sb = new StringBuilder();
            for (String f : lista) {
                sb.append(f).append("\n");
            }

            JOptionPane.showMessageDialog(this, sb.toString(), "Bidalitako formularioak", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error formularioak jasotzerakoan.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
public static void main(String[] args) {
    try {
        Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.115.67/erronka2?useSSL=false&serverTimezone=UTC", "Julen", "Julen12*");
        new MainFrame(conn);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
}