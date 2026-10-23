package cl.duocuc.supermercado.modelo;

import cl.duocuc.supermercado.enums.Medida;
import java.io.Serializable;

// [REQUISITO: Herencia]

public class Limpieza extends Producto implements Serializable {

    private static final long serialVersionUID = 1L;

    // [REQUISITO: Static y Final]
    private static final double IMPUESTO_TOXICO = 1.10;
    private static final double DESCUENTO_NO_TOXICO = 0.95;

    private boolean esToxico;

    public Limpieza(String idProducto, String denominacion, double valorBase,
                    int stock, Medida unidad, boolean esToxico) {
        super(idProducto, denominacion, valorBase, stock, unidad);
        this.esToxico = esToxico;
    }

    @Override
    public double calcularPrecio() {
        // [REQUISITO: Static y Final]
        if (esToxico) {
            return valorBase * IMPUESTO_TOXICO;
        }
        return valorBase * DESCUENTO_NO_TOXICO;
    }

    public boolean isEsToxico() { return esToxico; }
    public void setEsToxico(boolean esToxico) { this.esToxico = esToxico; }
}
