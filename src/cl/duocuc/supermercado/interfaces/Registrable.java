package cl.duocuc.supermercado.interfaces;

//interfaz para definir el comportamiento de objetos que pueden ser persistidos
public interface Registrable {

    //Guarda los datos del objeto en un medio persistente
    void guardarDatos();

    //Carga los datos del objeto desde un medio persistente
    void cargarDatos();
}