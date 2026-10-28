package cl.duocuc.supermercado.util;

import cl.duocuc.supermercado.excepciones.DatoInvalidoException;

/**
 * Utilidad centralizada para validaciones comunes del sistema.
 * Evita duplicar lógica de validación en múltiples clases de modelo.
 */
public final class Validador {

    // [REQUISITO: Static y Final]
    private Validador() {
        // Clase utilitaria: no debe instanciarse.
    }

    /**
     * Valida que una cadena no sea nula ni esté vacía.
     *
     * @param valor   cadena a validar
     * @param mensaje mensaje de error si la validación falla
     */
    public static void validarNoVacio(String valor, String mensaje) {
        if (valor == null || valor.isEmpty()) {
            throw new DatoInvalidoException(mensaje);
        }
    }

    /**
     * Valida que un objeto no sea nulo.
     *
     * @param objeto  objeto a validar
     * @param mensaje mensaje de error si la validación falla
     */
    public static void validarNoNulo(Object objeto, String mensaje) {
        if (objeto == null) {
            throw new DatoInvalidoException(mensaje);
        }
    }

    /**
     * Valida que un número double no sea negativo.
     *
     * @param valor   número a validar
     * @param mensaje mensaje de error si la validación falla
     */
    public static void validarNoNegativo(double valor, String mensaje) {
        if (valor < 0) {
            throw new DatoInvalidoException(mensaje);
        }
    }

    /**
     * Valida que un número entero no sea negativo.
     *
     * @param valor   número a validar
     * @param mensaje mensaje de error si la validación falla
     */
    public static void validarNoNegativo(int valor, String mensaje) {
        if (valor < 0) {
            throw new DatoInvalidoException(mensaje);
        }
    }
}
