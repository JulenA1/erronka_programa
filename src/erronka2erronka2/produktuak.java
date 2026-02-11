package erronka2erronka2;

public class produktuak {

    private int id;
    private String izena;
    private String deskripzioa;
    private double prezioa;
    private String egoera; // konponduta, estropeatu, aztertzeko
    private int stock;
    private String irudia;
    private Integer hornitzaile_id;

    public produktuak(int id, String izena, String deskripzioa, double prezioa,
                     String egoera, int stock, String irudia, Integer hornitzaile_id) {
        this.id = id;
        this.izena = izena;
        this.deskripzioa = deskripzioa;
        this.prezioa = prezioa;
        this.egoera = egoera;
        this.stock = stock;
        this.irudia = irudia;
        this.hornitzaile_id = hornitzaile_id;
    }

    public boolean dagoEskuragarri() {
        return stock > 0 && egoera.equals("konponduta");
    }
}
