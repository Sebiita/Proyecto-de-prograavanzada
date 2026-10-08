package cl.duocuc.supermercado.modelo;

import cl.duocuc.supermercado.enums.Medida;
import java.io.Serializable;

// [REQUISITO: Herencia]

public class Limpieza extends Producto implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean esToxico;

    public Limpieza(String idProducto, String denominacion, double valorBase,
                    int stock, Medida unidad, boolean esToxico) {
        super(idProducto, denominacion, valorBase, stock, unidad);
        this.esToxico = esToxico;
    }

    @Override
    public double calcularPrecio() {
        double precioFinal;
        if (esToxico) {
            precioFinal = valorBase * 1.10;
        } else {
            precioFinal = valorBase * 0.95;
        }
        return precioFinal;
    }

    public boolean isEsToxico() { return esToxico; }
    public void setEsToxico(boolean esToxico) { this.esToxico = esToxico; }
}
