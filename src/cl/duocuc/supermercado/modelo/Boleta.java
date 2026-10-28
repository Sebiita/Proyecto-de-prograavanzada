package cl.duocuc.supermercado.modelo;

import cl.duocuc.supermercado.enums.CategoriaOferta;
import cl.duocuc.supermercado.excepciones.DatoInvalidoException;
import cl.duocuc.supermercado.util.Validador;
import java.io.Serializable;
import java.util.ArrayList;

// [REQUISITO: Herencia] — llamada polimorfica a calcularPrecio()

public class Boleta implements Serializable {

    private static final long serialVersionUID = 1L;

    // [REQUISITO: Static y Final]
    private static final double REBAJA_PORCENTUAL = 0.10;
    private static final double DESCUENTO_DOS_POR_UNO = 0.50;

    private String idBoleta;
    private Comprador comprador;
    private ArrayList<Producto> listaCompra;
    private double totalPagar;
    private CategoriaOferta categoriaOferta;

    public Boleta(String idBoleta, Comprador comprador, CategoriaOferta categoriaOferta) {
        // [REQUISITO: Excepciones]
        Validador.validarNoVacio(idBoleta, "El ID de la boleta no puede ser vacío.");
        Validador.validarNoNulo(categoriaOferta, "La categoría de oferta es obligatoria.");

        this.idBoleta = idBoleta;
        this.comprador = comprador;
        this.categoriaOferta = categoriaOferta;
        this.listaCompra = new ArrayList<>();
        this.totalPagar = 0.0;
    }

    public void agregarProducto(Producto producto, int cantidad) {
        // [REQUISITO: Excepciones]
        Validador.validarNoNulo(producto, "El producto a agregar no puede ser nulo.");
        if (cantidad <= 0) {
            throw new DatoInvalidoException("La cantidad debe ser mayor a cero.");
        }
        int i = 0;
        while (i < cantidad) {
            listaCompra.add(producto);
            i = i + 1;
        }
    }

    public void procesarPago() {
        // [REQUISITO: Excepciones]
        if (listaCompra.isEmpty()) {
            throw new DatoInvalidoException("No se puede procesar una boleta sin productos.");
        }

        double suma = 0;
        int i = 0;
        while (i < listaCompra.size()) {
            suma = suma + listaCompra.get(i).calcularPrecio(); // [REQUISITO: Herencia]
            i = i + 1;
        }

        double rebaja = 0;
        if (categoriaOferta == CategoriaOferta.REBAJA_PORCENTUAL) {
            rebaja = suma * REBAJA_PORCENTUAL;
        } else if (categoriaOferta == CategoriaOferta.DOS_POR_UNO) {
            int pares = listaCompra.size() / 2;
            double precioUnitarioPromedio = suma / listaCompra.size();
            rebaja = pares * precioUnitarioPromedio * DESCUENTO_DOS_POR_UNO;
        }

        this.totalPagar = suma - rebaja;

        i = 0;
        while (i < listaCompra.size()) {
            listaCompra.get(i).descontarStock(1);
            i = i + 1;
        }

        if (comprador != null) {
            comprador.agregarBoleta(this);
        }

        System.out.println("Boleta " + idBoleta + " procesada. Total: $" + String.format("%.2f", totalPagar));
    }

    @Override
    public String toString() {
        return "Boleta " + idBoleta
                + " | Comprador: " + (comprador != null ? comprador.getAlias() : "N/A")
                + " | Productos: " + listaCompra.size()
                + " | Total: $" + String.format("%.2f", totalPagar)
                + " | Oferta: " + categoriaOferta;
    }

    // Getters y Setters
    public String getIdBoleta() { return idBoleta; }
    public void setIdBoleta(String idBoleta) {
        Validador.validarNoVacio(idBoleta, "El ID de la boleta no puede ser vacío.");
        this.idBoleta = idBoleta;
    }

    public Comprador getComprador() { return comprador; }
    public void setComprador(Comprador comprador) { this.comprador = comprador; }

    public ArrayList<Producto> getListaCompra() { return listaCompra; }
    public void setListaCompra(ArrayList<Producto> listaCompra) { this.listaCompra = listaCompra; }

    public double getTotalPagar() { return totalPagar; }
    public void setTotalPagar(double totalPagar) { this.totalPagar = totalPagar; }

    public CategoriaOferta getCategoriaOferta() { return categoriaOferta; }
    public void setCategoriaOferta(CategoriaOferta categoriaOferta) { this.categoriaOferta = categoriaOferta; }
}
