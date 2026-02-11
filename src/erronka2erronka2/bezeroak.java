package erronka2erronka2;

public class bezeroak {

    private int id;
    private String izena;
    private String abizenak;
    private String nan;
    private String helbidea;
    private String email; 
    private String erabiltzailea;
    private String pasahitza;
    private String rol; // bezero | hornitzaile

    public bezeroak(int id, String izena, String abizenak, String nan,
                   String helbidea, String email, String erabiltzailea,
                   String pasahitza, String rol) {
        this.id = id;
        this.izena = izena;
        this.abizenak = abizenak;
        this.nan = nan;
        this.helbidea = helbidea;
        this.email = email;
        this.erabiltzailea = erabiltzailea;
        this.pasahitza = pasahitza;
        this.rol = rol;
    }

    // Getters y setters
    public int getId() { return id; }
    public String getIzena() { return izena; }
    public String getAbizenak() { return abizenak; }
    public String getEmail() { return email; }
    public String getRol() { return rol; }
}
