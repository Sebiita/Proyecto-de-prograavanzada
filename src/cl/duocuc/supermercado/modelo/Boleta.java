package cl.duocuc.supermercado.modelo;

import cl.duocuc.supermercado.enums.CategoriaOferta;
import java.io.Serializable;
import java.util.ArrayList;

// [REQUISITO: Herencia] — llamada polimorfica a calcularPrecio()

public class Boleta implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idBoleta;
    private Comprador comprador;
    private ArrayList<Producto> listaCompra;
    private double totalPagar;
    private CategoriaOferta categoriaOferta;

    public Boleta(String idBoleta, Comprador comprador, CategoriaOferta categoriaOferta) {
        this.idBoleta = idBoleta;
        this.comprador = comprador;
        this.categoriaOferta = categoriaOferta;
        this.listaCompra = new ArrayList<>();
        this.totalPagar = 0.0;
    }

    public void agregarProducto(Producto producto, int cantidad) {
        int i = 0;
        while (i < cantidad) {
            listaCompra.add(producto);
            i = i + 1;
        }
    }

    public void procesarPago() {
        double suma = 0;
        int i = 0;
        while (i < listaCompra.size()) {
            suma = suma + listaCompra.get(i).calcularPrecio(); // [REQUISITO: Herencia]
            i = i + 1;
        }

        double rebaja = 0;
        if (categoriaOferta == CategoriaOferta.REBAJA_PORCENTUAL) {
            rebaja = suma * 0.10;
        } else if (categoriaOferta == CategoriaOferta.DOS_POR_UNO) {
            rebaja = (listaCompra.size() / 2) * (suma / listaCompra.size());
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

    // Getters y Setters
    public String getIdBoleta() { return idBoleta; }
    public void setIdBoleta(String idBoleta) { this.idBoleta = idBoleta; }

    public Comprador getComprador() { return comprador; }
    public void setComprador(Comprador comprador) { this.comprador = comprador; }

    public ArrayList<Producto> getListaCompra() { return listaCompra; }
    public void setListaCompra(ArrayList<Producto> listaCompra) { this.listaCompra = listaCompra; }

    public double getTotalPagar() { return totalPagar; }
    public void setTotalPagar(double totalPagar) { this.totalPagar = totalPagar; }

    public CategoriaOferta getCategoriaOferta() { return categoriaOferta; }
    public void setCategoriaOferta(CategoriaOferta categoriaOferta) { this.categoriaOferta = categoriaOferta; }
}
