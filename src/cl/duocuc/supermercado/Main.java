package cl.duocuc.supermercado;

import cl.duocuc.supermercado.sistema.ControladorSupermercado;

// [REQUISITO: Package]

/**
 * Punto de entrada del sistema de gestión del supermercado.
 */
public class Main {

    public static void main(String[] args) {
        ControladorSupermercado controlador = new ControladorSupermercado();

        // Intenta cargar datos previos; si no existen, precarga datos de prueba.
        controlador.getGuardador().cargarDatos();

        if (controlador.getInventarioTienda().getMapaProductos().isEmpty()
                && controlador.getListaCompradores().isEmpty()) {
            controlador.precargarDatos();
        }

        // Inicia el menú interactivo.
        controlador.iniciarMenu();
    }
}
// lol quiero ver si esta bien