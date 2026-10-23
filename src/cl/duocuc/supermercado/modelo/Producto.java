package cl.duocuc.supermercado.modelo;

import cl.duocuc.supermercado.enums.Medida;
import cl.duocuc.supermercado.excepciones.DatoInvalidoException;
import cl.duocuc.supermercado.interfaces.Rankeable;
import java.io.Serializable;

// [REQUISITO: Herencia]

public abstract class Producto implements Rankeable, Serializable {

    private static final long serialVersionUID = 1L;

    protected String idProducto;
    protected String denominacion;
    protected double valorBase;
    protected int stock;
    protected Medida unidad;
    protected int vendidos;

    public Producto(String idProducto, String denominacion, double valorBase,
                    int stock, Medida unidad) {
        // [REQUISITO: Excepciones]
        validarIdProducto(idProducto);
        validarDenominacion(denominacion);
        validarValorBase(valorBase);
        validarStock(stock);
        if (unidad == null) {
            throw new DatoInvalidoException("La unidad de medida es obligatoria.");
        }

        this.idProducto = idProducto;
        this.denominacion = denominacion;
        this.valorBase = valorBase;
        this.stock = stock;
        this.unidad = unidad;
        this.vendidos = 0;
    }

    public abstract double calcularPrecio();

    public void descontarStock(int cantidad) {
        // [REQUISITO: Excepciones]
        if (cantidad <= 0) {
            throw new DatoInvalidoException("La cantidad a descontar debe ser mayor a cero.");
        }
        if (cantidad > this.stock) {
            throw new DatoInvalidoException("No hay suficiente stock. Stock actual: " + this.stock + ", cantidad solicitada: " + cantidad);
        }
        this.stock = this.stock - cantidad;
        this.vendidos = this.vendidos + cantidad;
    }

    @Override
    public double obtenerCriterioRanking() {
        return this.vendidos * this.calcularPrecio();
    }

    @Override
    public String toString() {
        return "[" + idProducto + "] " + denominacion
                + " | Precio: $" + String.format("%.2f", calcularPrecio())
                + " | Stock: " + stock + " " + unidad
                + " | Vendidos: " + vendidos;
    }

    // Métodos de validación reutilizables
    // [REQUISITO: Static y Final]
    private static void validarIdProducto(String idProducto) {
        if (idProducto == null || idProducto.isEmpty()) {
            throw new DatoInvalidoException("El ID del producto no puede ser vacío.");
        }
    }

    // [REQUISITO: Static y Final]
    private static void validarDenominacion(String denominacion) {
        if (denominacion == null || denominacion.isEmpty()) {
            throw new DatoInvalidoException("La denominación del producto no puede ser vacía.");
        }
    }

    // [REQUISITO: Static y Final]
    private static void validarValorBase(double valorBase) {
        if (valorBase < 0) {
            throw new DatoInvalidoException("El valor base no puede ser negativo.");
        }
    }

    // [REQUISITO: Static y Final]
    private static void validarStock(int stock) {
        if (stock < 0) {
            throw new DatoInvalidoException("El stock no puede ser negativo.");
        }
    }

    // Getters y Setters
    public String getIdProducto() { return idProducto; }
    public void setIdProducto(String idProducto) {
        validarIdProducto(idProducto);
        this.idProducto = idProducto;
    }

    public String getDenominacion() { return denominacion; }
    public void setDenominacion(String denominacion) {
        validarDenominacion(denominacion);
        this.denominacion = denominacion;
    }

    public double getValorBase() { return valorBase; }
    public void setValorBase(double valorBase) {
        validarValorBase(valorBase);
        this.valorBase = valorBase;
    }

    public int getStock() { return stock; }
    public void setStock(int stock) {
        validarStock(stock);
        this.stock = stock;
    }

    public Medida getUnidad() { return unidad; }
    public void setUnidad(Medida unidad) {
        if (unidad == null) {
            throw new DatoInvalidoException("La unidad de medida es obligatoria.");
        }
        this.unidad = unidad;
    }

    public int getVendidos() { return vendidos; }
    public void setVendidos(int vendidos) {
        if (vendidos < 0) {
            throw new DatoInvalidoException("La cantidad de vendidos no puede ser negativa.");
        }
        this.vendidos = vendidos;
    }
}
