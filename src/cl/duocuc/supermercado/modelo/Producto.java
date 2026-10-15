package cl.duocuc.supermercado.modelo;

import cl.duocuc.supermercado.enums.Medida;
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
        if (idProducto == null || idProducto.isEmpty()) {
            throw new IllegalArgumentException("El ID del producto no puede ser vacío.");
        }
        if (denominacion == null || denominacion.isEmpty()) {
            throw new IllegalArgumentException("La denominación del producto no puede ser vacía.");
        }
        if (valorBase < 0) {
            throw new IllegalArgumentException("El valor base no puede ser negativo.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        if (unidad == null) {
            throw new IllegalArgumentException("La unidad de medida es obligatoria.");
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

    // Getters y Setters
    public String getIdProducto() { return idProducto; }
    public void setIdProducto(String idProducto) { this.idProducto = idProducto; }

    public String getDenominacion() { return denominacion; }
    public void setDenominacion(String denominacion) { this.denominacion = denominacion; }

    public double getValorBase() { return valorBase; }
    public void setValorBase(double valorBase) { this.valorBase = valorBase; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public Medida getUnidad() { return unidad; }
    public void setUnidad(Medida unidad) { this.unidad = unidad; }

    public int getVendidos() { return vendidos; }
    public void setVendidos(int vendidos) { this.vendidos = vendidos; }
}
