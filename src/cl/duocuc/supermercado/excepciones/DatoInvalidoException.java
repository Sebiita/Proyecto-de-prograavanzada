package cl.duocuc.supermercado.excepciones;

// [REQUISITO: Excepciones]

/**
 * Excepción lanzada cuando se proporciona un dato inválido
 * al crear o modificar un objeto del sistema.
 */
public class DatoInvalidoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DatoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
