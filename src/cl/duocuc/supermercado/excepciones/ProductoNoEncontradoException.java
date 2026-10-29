package cl.duocuc.supermercado.excepciones;

//excepción lanzada cuando se intenta operar con un productoque no existe en el inventario
public class ProductoNoEncontradoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ProductoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}