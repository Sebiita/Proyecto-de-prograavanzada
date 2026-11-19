package cl.duocuc.supermercado.ui.vistas;

import cl.duocuc.supermercado.sistema.ControladorSupermercado;
import cl.duocuc.supermercado.ui.BaseFrame;

@SuppressWarnings("serial")
public class ProductosVista extends BaseFrame {

    public ProductosVista(ControladorSupermercado controlador) {
        super("Supermercado | Productos", controlador);
        PanelProductos productos = new PanelProductos(servicio);
        productos.actualizar();
        agregarContenido(productos);
    }

    @Override
    public void mostrar() {
        mostrarVentana();
    }
}
