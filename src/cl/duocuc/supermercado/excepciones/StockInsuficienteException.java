package cl.duocuc.supermercado.excepciones;

//excepción lanzada cuando no hay stock suficiente para realizar una venta
public class StockInsuficienteException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public StockInsuficienteException(int actual, int pedido) {
        super("Stock insuficiente: hay " + actual + ", se pidieron " + pedido + ".");
    }
}