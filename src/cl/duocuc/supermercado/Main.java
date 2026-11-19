package cl.duocuc.supermercado;

import cl.duocuc.supermercado.sistema.ControladorSupermercado;
import cl.duocuc.supermercado.ui.vistas.InicioVista;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        ControladorSupermercado controlador = new ControladorSupermercado();

        controlador.getGuardador().cargarDatos();

        if (controlador.getInventarioTienda().getMapaProductos().isEmpty()
                && controlador.getListaCompradores().isEmpty()) {
            controlador.precargarDatos();
        }

        if (args.length > 0 && "--consola".equalsIgnoreCase(args[0])) {

            controlador.iniciarMenu();
        } else {
            SwingUtilities.invokeLater(() -> new InicioVista(controlador).mostrar());
        }
    }
}
