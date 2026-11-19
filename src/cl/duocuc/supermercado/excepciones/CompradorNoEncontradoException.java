package cl.duocuc.supermercado.excepciones;

public class CompradorNoEncontradoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CompradorNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
