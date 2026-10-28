package cl.duocuc.supermercado.excepciones;

// [REQUISITO: Excepciones]

/**
 * Excepción lanzada cuando se intenta operar con un comprador
 * que no está registrado en el sistema.
 */
public class CompradorNoEncontradoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CompradorNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
