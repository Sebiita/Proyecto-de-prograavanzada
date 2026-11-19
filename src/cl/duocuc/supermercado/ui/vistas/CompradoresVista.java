package cl.duocuc.supermercado.ui.vistas;

import cl.duocuc.supermercado.sistema.ControladorSupermercado;
import cl.duocuc.supermercado.ui.BaseFrame;

@SuppressWarnings("serial")
public class CompradoresVista extends BaseFrame {

    public CompradoresVista(ControladorSupermercado controlador) {
        super("Supermercado | Compradores", controlador);
        PanelCompradores compradores = new PanelCompradores(servicio);
        compradores.actualizar();
        agregarContenido(compradores);
    }

    @Override
    public void mostrar() {
        mostrarVentana();
    }
}
