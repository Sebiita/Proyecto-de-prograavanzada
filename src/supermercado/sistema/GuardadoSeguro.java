package supermercado.sistema;

import supermercado.interfaces.Registrable;
import supermercado.modelo.Boleta;
import supermercado.modelo.Comprador;
import supermercado.modelo.Inventario;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Scanner;

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

            if (completarIdsCompradores()) {
                guardarDatos();
            }

            System.out.println("Datos cargados exitosamente desde: " + RUTA_DATOS);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar los datos: " + e.getMessage());
        }
    }

    private boolean completarIdsCompradores() {
        ArrayList<Comprador> compradores = ctrl.getListaCompradores();
        Set<Integer> idsUsados = new HashSet<>();
        int mayorId = 0;

        for (Comprador comprador : compradores) {
            String id = comprador.getIdComprador();
            if (id == null || !id.matches("CLI-\\d+")) {
                continue;
            }
            int numero = Integer.parseInt(id.substring(4));
            idsUsados.add(numero);
            mayorId = Math.max(mayorId, numero);
        }

        boolean modificados = false;
        int siguienteLibre = 1;
        for (Comprador comprador : compradores) {
            String id = comprador.getIdComprador();
            if (id != null && id.matches("CLI-\\d+")) {
                continue;
            }
            while (idsUsados.contains(siguienteLibre)) {
                siguienteLibre++;
            }
            comprador.setIdComprador("CLI-" + String.format("%03d", siguienteLibre));
            idsUsados.add(siguienteLibre);
            mayorId = Math.max(mayorId, siguienteLibre);
            siguienteLibre++;
            modificados = true;
        }

        if (modificados && ControladorSupermercado.getContadorCompradores() <= mayorId) {
            ControladorSupermercado.setContadorCompradores(mayorId + 1);
        }
        return modificados;
    }

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
