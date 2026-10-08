package cl.duocuc.supermercado.interfaces;

// [REQUISITO: Interface]

/**
 * Interfaz que define el comportamiento de objetos que pueden ser rankeados.
 */
public interface Rankeable {

    /**
     * Obtiene el criterio numérico utilizado para el ranking.
     *
     * @return valor double que representa el criterio de ranking.
     */
    double obtenerCriterioRanking();
}
