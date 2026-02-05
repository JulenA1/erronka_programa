package erronka2erronka2;

public class pedidoproduktua {

    private int id;
    private int pedidoId;
    private int produktuId;
    private String izena;
    private int kopurua;
    private double kostuTotala;

    public pedidoproduktua(int id, int pedidoId, int produktuId,
                            String izena, int kopurua, double kostuTotala) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.produktuId = produktuId;
        this.izena = izena;
        this.kopurua = kopurua;
        this.kostuTotala = kostuTotala;
    }
}
