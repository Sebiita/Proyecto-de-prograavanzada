package cl.duocuc.supermercado;

import cl.duocuc.supermercado.sistema.ControladorSupermercado;
import cl.duocuc.supermercado.ui.vistas.InicioVista;
import javax.swing.SwingUtilities;

//Punto de entrada del sistema de gestión del supermercado
public class Main {

    public static void main(String[] args) {
        ControladorSupermercado controlador = new ControladorSupermercado();

        //llamada para la persistencia de datos
        //aqui intenta cargar datos previos, si no existen precarga datos de prueba
        controlador.getGuardador().cargarDatos();

        if (controlador.getInventarioTienda().getMapaProductos().isEmpty()
                && controlador.getListaCompradores().isEmpty()) {
            controlador.precargarDatos();
        }

        if (args.length > 0 && "--consola".equalsIgnoreCase(args[0])) {
            // Mantiene disponible la interfaz de consola de la Unidad II.
            controlador.iniciarMenu();
        } else {
            SwingUtilities.invokeLater(() -> new InicioVista(controlador).mostrar());
        }
    }
}
