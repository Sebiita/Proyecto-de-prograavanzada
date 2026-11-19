package cl.duocuc.supermercado.util;

import cl.duocuc.supermercado.excepciones.DatoInvalidoException;

public final class Validador {

    private Validador() {

    }

    public static void validarNoVacio(String valor, String mensaje) {
        if (valor == null || valor.isEmpty()) {
            throw new DatoInvalidoException(mensaje);
        }
    }

    public static void validarNoNulo(Object objeto, String mensaje) {
        if (objeto == null) {
            throw new DatoInvalidoException(mensaje);
        }
    }

    public static void validarNoNegativo(double valor, String mensaje) {
        if (valor < 0) {
            throw new DatoInvalidoException(mensaje);
        }
    }

    public static void validarNoNegativo(int valor, String mensaje) {
        if (valor < 0) {
            throw new DatoInvalidoException(mensaje);
        }
    }
}
