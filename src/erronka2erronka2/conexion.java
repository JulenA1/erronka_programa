package erronka2erronka2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class conexion {
    private static Connection conn = null;

    public static Connection getConnection() {
        if (conn == null) {
            try {
            	String url = "jdbc:mysql://127.0.0.1:3306/erronka2?useSSL=false&serverTimezone=UTC";
                String user = "Julen";
                String pass = "Julen12*"; // tu contraseña de XAMPP
                conn = DriverManager.getConnection(url, user, pass);
                System.out.println("Conexión a la base de datos establecida.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }
}
