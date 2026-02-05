package erronka2erronka2;

import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Clase base
public class langileak {
    protected int Ln_id;
    protected String izena;
    protected String abizenak;
    protected String herrialdea;
    protected String email;
    protected int posta_kodea;
    protected String erabiltzailea;
    protected String pasahitza;
    protected String rola;

    protected Connection conn;

    // Constructor con conexión
    public langileak(Connection conn) {
        this.conn = conn;
    }

    // Constructor con todos los datos
    public langileak(int Ln_id, String izena, String abizenak, String herrialdea, String email,
            int posta_kodea, String erabiltzailea, String pasahitza, String rola,
            Connection conn) {
this.Ln_id = Ln_id;
this.izena = izena;
this.abizenak = abizenak;
this.herrialdea = herrialdea;
this.email = email;
this.posta_kodea = posta_kodea;
this.erabiltzailea = erabiltzailea;
this.pasahitza = pasahitza;
this.rola = rola;
this.conn = conn;
}
}
