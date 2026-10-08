package cl.duocuc.supermercado.modelo;

import java.io.Serializable;
import java.util.HashMap;

// [REQUISITO: Colecciones]

/**
 * Representa el inventario de productos del supermercado.
 * Utiliza HashMap para búsquedas eficientes por ID.
 */
public class Inventario implements Serializable {

    private static final long serialVersionUID = 1L;

    private HashMap<String, Producto> mapaProductos;

    public Inventario() {
        this.mapaProductos = new HashMap<>();
    }

    public Producto buscarProductoPorId(String id) {
        return mapaProductos.get(id);
    }

    public void agregarProducto(Producto p) {
        mapaProductos.put(p.getIdProducto(), p);
    }

    public HashMap<String, Producto> getMapaProductos() {
        return mapaProductos;
    }

    public void setMapaProductos(HashMap<String, Producto> mapaProductos) {
        this.mapaProductos = mapaProductos;
    }
}
