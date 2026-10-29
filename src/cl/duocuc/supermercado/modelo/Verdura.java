package cl.duocuc.supermercado.modelo;

import cl.duocuc.supermercado.enums.Medida;
import java.io.Serializable;

//requisito herencia (clase hijo)

public class Verdura extends Producto implements Serializable {

    private static final long serialVersionUID = 1L;

    //requisito static y final
    private static final double RECARGO_ORGANICO = 1.20;

    private boolean esOrganica;

    public Verdura(String idProducto, String denominacion, double valorBase,
                   int stock, Medida unidad, boolean esOrganica) {
        super(idProducto, denominacion, valorBase, stock, unidad);
        this.esOrganica = esOrganica;
    }

    @Override
    public double calcularPrecio() {
        if (esOrganica) {
            return valorBase * RECARGO_ORGANICO;
        }
        return valorBase;
    }

    public boolean isEsOrganica() { return esOrganica; }
    public void setEsOrganica(boolean esOrganica) { this.esOrganica = esOrganica; }
}