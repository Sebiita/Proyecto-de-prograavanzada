package cl.duocuc.supermercado.modelo;

import cl.duocuc.supermercado.enums.Medida;
import cl.duocuc.supermercado.excepciones.DatoInvalidoException;
import cl.duocuc.supermercado.interfaces.Rankeable;
import cl.duocuc.supermercado.util.Validador;
import java.io.Serializable;

//requisito herencia (clase padre)

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
        //REQUISITO: Excepciones
        Validador.validarNoVacio(idProducto, "El ID del producto no puede ser vacío.");
        Validador.validarNoVacio(denominacion, "La denominación del producto no puede ser vacía.");
        Validador.validarNoNegativo(valorBase, "El valor base no puede ser negativo.");
        Validador.validarNoNegativo(stock, "El stock no puede ser negativo.");
        Validador.validarNoNulo(unidad, "La unidad de medida es obligatoria.");

        this.idProducto = idProducto;
        this.denominacion = denominacion;
        this.valorBase = valorBase;
        this.stock = stock;
        this.unidad = unidad;
        this.vendidos = 0;
    }

    public abstract double calcularPrecio();

    public void descontarStock(int cantidad) {
        //REQUISITO: Excepciones
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

    //Getters y Setters
    public String getIdProducto() { return idProducto; }
    public void setIdProducto(String idProducto) {
        Validador.validarNoVacio(idProducto, "El ID del producto no puede ser vacío.");
        this.idProducto = idProducto;
    }

    public String getDenominacion() { return denominacion; }
    public void setDenominacion(String denominacion) {
        Validador.validarNoVacio(denominacion, "La denominación del producto no puede ser vacía.");
        this.denominacion = denominacion;
    }

    public double getValorBase() { return valorBase; }
    public void setValorBase(double valorBase) {
        Validador.validarNoNegativo(valorBase, "El valor base no puede ser negativo.");
        this.valorBase = valorBase;
    }

    public int getStock() { return stock; }
    public void setStock(int stock) {
        Validador.validarNoNegativo(stock, "El stock no puede ser negativo.");
        this.stock = stock;
    }

    public Medida getUnidad() { return unidad; }
    public void setUnidad(Medida unidad) {
        Validador.validarNoNulo(unidad, "La unidad de medida es obligatoria.");
        this.unidad = unidad;
    }

    public int getVendidos() { return vendidos; }
    public void setVendidos(int vendidos) {
        Validador.validarNoNegativo(vendidos, "La cantidad de vendidos no puede ser negativa.");
        this.vendidos = vendidos;
    }
}