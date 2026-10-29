package cl.duocuc.supermercado.modelo;

import cl.duocuc.supermercado.enums.Medida;
import java.io.Serializable;

//requisito herencia (clase hijo)
public class Lacteo extends Producto implements Serializable {

    private static final long serialVersionUID = 1L;

    //requisito de final y static
    private static final double RECARGO_FRIO = 1.15;

    private boolean requiereFrio;

    public Lacteo(String idProducto, String denominacion, double valorBase,
                  int stock, Medida unidad, boolean requiereFrio) {
        super(idProducto, denominacion, valorBase, stock, unidad);
        this.requiereFrio = requiereFrio;
    }

    @Override
    public double calcularPrecio() {
        if (requiereFrio) {
            return valorBase * RECARGO_FRIO;
        }
        return valorBase;
    }

    public boolean isRequiereFrio() { return requiereFrio; }
    public void setRequiereFrio(boolean requiereFrio) { this.requiereFrio = requiereFrio; }
}