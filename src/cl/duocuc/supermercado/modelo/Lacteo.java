package cl.duocuc.supermercado.modelo;

import cl.duocuc.supermercado.enums.Medida;
import java.io.Serializable;

// [REQUISITO: Herencia]

public class Lacteo extends Producto implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean requiereFrio;

    public Lacteo(String idProducto, String denominacion, double valorBase,
                  int stock, Medida unidad, boolean requiereFrio) {
        super(idProducto, denominacion, valorBase, stock, unidad);
        this.requiereFrio = requiereFrio;
    }

    @Override
    public double calcularPrecio() {
        double precioFinal;
        if (requiereFrio) {
            precioFinal = valorBase * 1.15;
        } else {
            precioFinal = valorBase;
        }
        return precioFinal;
    }

    public boolean isRequiereFrio() { return requiereFrio; }
    public void setRequiereFrio(boolean requiereFrio) { this.requiereFrio = requiereFrio; }
}
