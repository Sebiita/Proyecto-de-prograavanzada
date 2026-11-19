package supermercado.ui.vistas;

import supermercado.sistema.ControladorSupermercado;
import supermercado.ui.BaseFrame;

@SuppressWarnings("serial")
public class VentasVista extends BaseFrame {

    public VentasVista(ControladorSupermercado controlador) {
        super("Supermercado | Ventas", controlador);
        PanelVentas ventas = new PanelVentas(servicio);
        ventas.actualizar();
        agregarContenido(ventas);
    }

    @Override
    public void mostrar() {
        mostrarVentana();
    }
}