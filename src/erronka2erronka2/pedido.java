package erronka2erronka2;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class pedido {

    private int id;
    private int bezeroId;
    private LocalDateTime fecha;
    private String estadoEntrega; // pendiente, enviado, entregado
    private double total;
    private List<pedidoproduktua> produktuak;

    public pedido(int id, int bezeroId, LocalDateTime fecha,
                  String estadoEntrega, double total) {
        this.id = id;
        this.bezeroId = bezeroId;
        this.fecha = fecha;
        this.estadoEntrega = estadoEntrega;
        this.total = total;
        this.produktuak = new ArrayList<>();
    }

    public void gehituProduktua(pedidoproduktua pp) {
        produktuak.add(pp);
    }

    public List<pedidoproduktua> getProduktuak() {
        return produktuak;
    }
}
