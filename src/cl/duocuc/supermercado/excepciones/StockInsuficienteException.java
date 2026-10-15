package cl.duocuc.supermercado.excepciones;

// [REQUISITO: Excepciones]

/**
 * Excepción lanzada cuando no hay stock suficiente para realizar una venta.
 */
public class StockInsuficienteException extends RuntimeException {

    public StockInsuficienteException(int actual, int pedido) {
        super("Stock insuficiente: hay " + actual + ", se pidieron " + pedido + ".");
    }
}
