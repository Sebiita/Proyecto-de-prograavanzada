package cl.duocuc.supermercado.sistema;

import cl.duocuc.supermercado.interfaces.Registrable;
import cl.duocuc.supermercado.modelo.Boleta;
import cl.duocuc.supermercado.modelo.Comprador;
import cl.duocuc.supermercado.modelo.Inventario;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

// [REQUISITO: Interface]
// [REQUISITO: Persistencia]

public class GuardadoSeguro implements Registrable {

    private static final String RUTA_DATOS = "data/supermercado.dat";

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
            System.out.println("Datos guardados exitosamente en: " + RUTA_DATOS);
        } catch (IOException e) {
            System.out.println("Error al guardar los datos: " + e.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void cargarDatos() {
        File archivo = new File(RUTA_DATOS);
        if (!archivo.exists()) {
            System.out.println("No existe archivo de datos previo. Se inicia con datos vacios.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            ctrl.setInventarioTienda((Inventario) ois.readObject());
            ctrl.setListaCompradores((ArrayList<Comprador>) ois.readObject());
            ctrl.setRegistroBoletas((ArrayList<Boleta>) ois.readObject());

            int i = 0;
            while (i < ctrl.getListaCompradores().size()) {
                String rut = ctrl.getListaCompradores().get(i).getRutComprador().intern();
                ctrl.getListaCompradores().get(i).setRutComprador(rut);
                i = i + 1;
            }

            System.out.println("Datos cargados exitosamente desde: " + RUTA_DATOS);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar los datos: " + e.getMessage());
        }
    }
}
