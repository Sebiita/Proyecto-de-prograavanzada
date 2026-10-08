package cl.duocuc.supermercado.modelo;

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

    // Getters y Setters
    public String getRutComprador() { return rutComprador; }
    public void setRutComprador(String rutComprador) { this.rutComprador = rutComprador; }

    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }

    public ArrayList<Boleta> getHistorialBoletas() { return historialBoletas; }
    public void setHistorialBoletas(ArrayList<Boleta> historialBoletas) { this.historialBoletas = historialBoletas; }
}
