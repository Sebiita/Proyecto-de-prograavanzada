package cl.duocuc.supermercado.sistema;

import cl.duocuc.supermercado.interfaces.Registrable;
import cl.duocuc.supermercado.modelo.Boleta;
import cl.duocuc.supermercado.modelo.Comprador;
import cl.duocuc.supermercado.modelo.Inventario;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

//REQUISITO: Interface
//REQUISITO: Persistencia

public class GuardadoSeguro implements Registrable {

    private static final String RUTA_DATOS = "data/supermercado.dat";
    private static final String RUTA_CONTADORES = "data/contadores.txt";

    private ControladorSupermercado ctrl;

    public GuardadoSeguro(ControladorSupermercado ctrl) {
        this.ctrl = ctrl;
    }

    @Override
    public void guardarDatos() {
        File archivo = new File(RUTA_DATOS);
        archivo.getParentFile().mkdirs();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(ctrl.getInventarioTienda());
            oos.writeObject(ctrl.getListaCompradores());
            oos.writeObject(ctrl.getRegistroBoletas());
            guardarContadores();
            System.out.println("Datos guardados exitosamente en: " + RUTA_DATOS);
        } catch (IOException e) {
            System.out.println("Error al guardar los datos: " + e.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void cargarDatos() {
        //REQUISITO: Persistencia
        File archivo = new File(RUTA_DATOS);
        if (!archivo.exists()) {
            System.out.println("No existe archivo de datos previo. Se inicia con datos vacios.");
            return;
        }
        if (archivo.length() == 0) {
            System.out.println("El archivo de datos está vacío. Se inicia con datos vacios.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            ctrl.setInventarioTienda((Inventario) ois.readObject());
            ctrl.setListaCompradores((ArrayList<Comprador>) ois.readObject());
            ctrl.setRegistroBoletas((ArrayList<Boleta>) ois.readObject());
            cargarContadores();

            System.out.println("Datos cargados exitosamente desde: " + RUTA_DATOS);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar los datos: " + e.getMessage());
        }
    }

    //REQUISITO: Static y Final
    //Guarda los contadores estáticos en un archivo de texto simple
    private void guardarContadores() {
        File archivo = new File(RUTA_CONTADORES);
        archivo.getParentFile().mkdirs();

        try (PrintWriter escritor = new PrintWriter(new FileWriter(archivo))) {
            escritor.println(ControladorSupermercado.getContadorProductos());
            escritor.println(ControladorSupermercado.getContadorBoletas());
            escritor.println(ControladorSupermercado.getContadorCompradores());
        } catch (IOException e) {
            System.out.println("Error al guardar contadores: " + e.getMessage());
        }
    }

    //REQUISITO: Static y Final
    //Carga los contadores estáticos desde un archivo de texto simple
    private void cargarContadores() {
        File archivo = new File(RUTA_CONTADORES);
        if (!archivo.exists()) {
            System.out.println("No existe archivo de contadores previo. Se usan valores por defecto.");
            return;
        }

        try (Scanner lector = new Scanner(archivo)) {
            if (lector.hasNextInt()) {
                ControladorSupermercado.setContadorProductos(lector.nextInt());
            }
            if (lector.hasNextInt()) {
                ControladorSupermercado.setContadorBoletas(lector.nextInt());
            }
            if (lector.hasNextInt()) {
                ControladorSupermercado.setContadorCompradores(lector.nextInt());
            }
        } catch (IOException e) {
            System.out.println("Error al cargar contadores: " + e.getMessage());
        }
    }
}
