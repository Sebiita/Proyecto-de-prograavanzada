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
