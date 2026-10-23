package cl.duocuc.supermercado.modelo;

import cl.duocuc.supermercado.excepciones.DatoInvalidoException;
import cl.duocuc.supermercado.interfaces.Rankeable;
import java.io.Serializable;
import java.util.ArrayList;

// [REQUISITO: Herencia]

public class Comprador implements Rankeable, Serializable {

    private static final long serialVersionUID = 1L;

    private String rutComprador;
    private String alias;
    private ArrayList<Boleta> historialBoletas;

    public Comprador(String rutComprador, String alias) {
        // [REQUISITO: Excepciones]
        validarRutComprador(rutComprador);
        validarAlias(alias);

        this.rutComprador = rutComprador;
        this.alias = alias;
        this.historialBoletas = new ArrayList<>();
    }

    // [REQUISITO: Static y Final]
    private static void validarRutComprador(String rutComprador) {
        if (rutComprador == null || rutComprador.isEmpty()) {
            throw new DatoInvalidoException("El RUT del comprador no puede ser vacío.");
        }
    }

    // [REQUISITO: Static y Final]
    private static void validarAlias(String alias) {
        if (alias == null || alias.isEmpty()) {
            throw new DatoInvalidoException("El alias del comprador no puede ser vacío.");
        }
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
        return "RUT: " + rutComprador + " | Alias: " + alias
                + " | Boletas: " + historialBoletas.size()
                + " | Total gastado: $" + String.format("%.2f", obtenerCriterioRanking());
    }

    // Getters y Setters
    public String getRutComprador() { return rutComprador; }
    public void setRutComprador(String rutComprador) {
        validarRutComprador(rutComprador);
        this.rutComprador = rutComprador;
    }

    public String getAlias() { return alias; }
    public void setAlias(String alias) {
        validarAlias(alias);
        this.alias = alias;
    }

    public ArrayList<Boleta> getHistorialBoletas() { return historialBoletas; }
    public void setHistorialBoletas(ArrayList<Boleta> historialBoletas) {
        if (historialBoletas == null) {
            throw new DatoInvalidoException("El historial de boletas no puede ser nulo.");
        }
        this.historialBoletas = historialBoletas;
    }
}
