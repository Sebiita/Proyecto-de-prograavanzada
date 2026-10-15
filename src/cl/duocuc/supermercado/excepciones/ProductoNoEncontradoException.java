package cl.duocuc.supermercado.excepciones;

// [REQUISITO: Excepciones]

/**
 * Excepción lanzada cuando se intenta operar con un producto
 * que no existe en el inventario.
 */
public class ProductoNoEncontradoException extends RuntimeException {

    public ProductoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
