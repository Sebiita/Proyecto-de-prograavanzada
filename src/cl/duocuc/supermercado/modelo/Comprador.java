package cl.duocuc.supermercado.modelo;

import cl.duocuc.supermercado.interfaces.Rankeable;
import cl.duocuc.supermercado.util.Validador;
import java.io.Serializable;
import java.util.ArrayList;

//requisito de herencia

public class Comprador implements Rankeable, Serializable {

    private static final long serialVersionUID = 1L;

    private String idComprador;
    private String rutComprador;
    private String alias;
    private ArrayList<Boleta> historialBoletas;

    public Comprador(String idComprador, String rutComprador, String alias) {
        Validador.validarNoVacio(idComprador, "El ID del comprador no puede ser vacío.");
        Validador.validarNoVacio(rutComprador, "El RUT del comprador no puede ser vacío.");
        Validador.validarNoVacio(alias, "El alias del comprador no puede ser vacío.");

        this.idComprador = idComprador;
        this.rutComprador = rutComprador;
        this.alias = alias;
        this.historialBoletas = new ArrayList<>();
    }

    public void agregarBoleta(Boleta b) {
        this.historialBoletas.add(b);
    }

    @Override
    public double obtenerCriterioRanking() {
        double acumulado = 0;
        int i = 0;
        while (i < historialBoletas.size()) {
            acumulado = acumulado + historialBoletas.get(i).getTotalPagar();
            i = i + 1;
        }
        return acumulado;
    }

    @Override
    public String toString() {
        return "[" + idComprador + "] RUT: " + rutComprador + " | Alias: " + alias
                + " | Boletas: " + historialBoletas.size()
                + " | Total gastado: $" + String.format("%.2f", obtenerCriterioRanking());
    }

    //Getters y setters
    public String getIdComprador() { return idComprador; }
    public void setIdComprador(String idComprador) {
        Validador.validarNoVacio(idComprador, "El ID del comprador no puede ser vacío.");
        this.idComprador = idComprador;
    }

    public String getRutComprador() { return rutComprador; }
    public void setRutComprador(String rutComprador) {
        Validador.validarNoVacio(rutComprador, "El RUT del comprador no puede ser vacío.");
        this.rutComprador = rutComprador;
    }

    public String getAlias() { return alias; }
    public void setAlias(String alias) {
        Validador.validarNoVacio(alias, "El alias del comprador no puede ser vacío.");
        this.alias = alias;
    }

    public ArrayList<Boleta> getHistorialBoletas() { return historialBoletas; }
    public void setHistorialBoletas(ArrayList<Boleta> historialBoletas) {
        Validador.validarNoNulo(historialBoletas, "El historial de boletas no puede ser nulo.");
        this.historialBoletas = historialBoletas;
    }
}