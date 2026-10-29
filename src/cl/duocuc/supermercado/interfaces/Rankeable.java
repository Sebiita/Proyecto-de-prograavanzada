package cl.duocuc.supermercado.interfaces;

//interfaz para definir el comportamiento de objetos que pueden ser rankeados
public interface Rankeable {
    //obtiene el criterio numérico utilizado para el ranking
    double obtenerCriterioRanking();
}