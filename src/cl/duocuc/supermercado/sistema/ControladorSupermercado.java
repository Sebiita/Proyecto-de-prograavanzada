package cl.duocuc.supermercado.sistema;

import cl.duocuc.supermercado.enums.CategoriaOferta;
import cl.duocuc.supermercado.enums.Medida;
import cl.duocuc.supermercado.interfaces.Registrable;
import cl.duocuc.supermercado.modelo.Boleta;
import cl.duocuc.supermercado.modelo.Comprador;
import cl.duocuc.supermercado.modelo.Inventario;
import cl.duocuc.supermercado.modelo.Lacteo;
import cl.duocuc.supermercado.modelo.Limpieza;
import cl.duocuc.supermercado.modelo.Producto;
import cl.duocuc.supermercado.modelo.Verdura;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

// [REQUISITO: Package]
// [REQUISITO: Interface]
// [REQUISITO: Colecciones]
// [REQUISITO: Menu - Scanner]
// [REQUISITO: Persistencia]

public class ControladorSupermercado implements Registrable, Serializable {

    private static final long serialVersionUID = 1L;

    private static final String RUTA_DATOS = "data/supermercado.dat";

    private Inventario inv;
    private ArrayList<Comprador> clientes;
    private ArrayList<Boleta> boletas;

    private transient Scanner scanner;

    public ControladorSupermercado() {
        this.inv = new Inventario();
        this.clientes = new ArrayList<>();
        this.boletas = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    public void precargarDatos() {
        inv.agregarProducto(new Lacteo("P001", "Leche Entera", 1200, 20, Medida.LITRO, true));
        inv.agregarProducto(new Lacteo("P002", "Yogur Natural", 800, 15, Medida.UNIDAD, true));
        inv.agregarProducto(new Verdura("P003", "Zanahoria", 500, 30, Medida.KILOGRAMO, false));
        inv.agregarProducto(new Verdura("P004", "Manzana Orgánica", 1200, 25, Medida.KILOGRAMO, true));
        inv.agregarProducto(new Limpieza("P005", "Detergente", 2500, 10, Medida.LITRO, false));
        inv.agregarProducto(new Limpieza("P006", "Cloro", 1500, 12, Medida.LITRO, true));

        clientes.add(new Comprador("12345678-9".intern(), "Seba ruiz"));
        clientes.add(new Comprador("98765432-1".intern(), "cristian ruiz"));

        System.out.println("Datos precargados correctamente.");
    }

    @Override
    public void guardarDatos() {
        // [REQUISITO: Persistencia]
        File archivo = new File(RUTA_DATOS);
        archivo.getParentFile().mkdirs();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(inv);
            oos.writeObject(clientes);
            oos.writeObject(boletas);
            System.out.println("Datos guardados exitosamente en: " + RUTA_DATOS);
        } catch (IOException e) {
            System.out.println("Error al guardar los datos: " + e.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void cargarDatos() {
        // [REQUISITO: Persistencia]
        File archivo = new File(RUTA_DATOS);
        if (!archivo.exists()) {
            System.out.println("No existe archivo de datos previo. Se inicia con datos vacíos.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            this.inv = (Inventario) ois.readObject();
            this.clientes = (ArrayList<Comprador>) ois.readObject();
            this.boletas = (ArrayList<Boleta>) ois.readObject();

            int i = 0;
            while (i < clientes.size()) {
                clientes.get(i).setRutComprador(clientes.get(i).getRutComprador().intern());
                i = i + 1;
            }

            System.out.println("Datos cargados exitosamente desde: " + RUTA_DATOS);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar los datos: " + e.getMessage());
        }
    }

    public void iniciarMenu() {
        // [REQUISITO: Menu - Scanner]
        if (scanner == null) {
            scanner = new Scanner(System.in);
        }

        int opcion;
        do {
            System.out.println("\n===== SUPERMERCADO =====");
            System.out.println("1. Agregar producto");
            System.out.println("2. Listar productos");
            System.out.println("3. Agregar comprador");
            System.out.println("4. Listar compradores");
            System.out.println("5. Realizar venta");
            System.out.println("6. Ver boletas");
            System.out.println("7. Guardar datos");
            System.out.println("8. Cargar datos");
            System.out.println("9. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    agregarProducto();
                    break;
                case 2:
                    listarProductos();
                    break;
                case 3:
                    agregarComprador();
                    break;
                case 4:
                    listarCompradores();
                    break;
                case 5:
                    realizarVenta();
                    break;
                case 6:
                    verBoletas();
                    break;
                case 7:
                    guardarDatos();
                    break;
                case 8:
                    cargarDatos();
                    break;
                case 9:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 9);
    }

    private void agregarProducto() {
        System.out.print("Ingrese ID del producto: ");
        String id = scanner.nextLine();
        System.out.print("Ingrese denominación: ");
        String denominacion = scanner.nextLine();
        System.out.print("Ingrese valor base: ");
        double valorBase = scanner.nextDouble();
        System.out.print("Ingrese cantidad disponible: ");
        int cantidad = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Seleccione tipo: 1. Lácteo 2. Verdura 3. Limpieza");
        int tipo = scanner.nextInt();
        scanner.nextLine();

        Producto producto = null;
        switch (tipo) {
            case 1:
                System.out.print("¿Requiere frío? (s/n): ");
                boolean frio = scanner.nextLine().equalsIgnoreCase("s");
                producto = new Lacteo(id, denominacion, valorBase, cantidad, Medida.UNIDAD, frio);
                break;
            case 2:
                System.out.print("¿Es orgánica? (s/n): ");
                boolean organica = scanner.nextLine().equalsIgnoreCase("s");
                producto = new Verdura(id, denominacion, valorBase, cantidad, Medida.KILOGRAMO, organica);
                break;
            case 3:
                System.out.print("¿Es tóxico? (s/n): ");
                boolean toxico = scanner.nextLine().equalsIgnoreCase("s");
                producto = new Limpieza(id, denominacion, valorBase, cantidad, Medida.LITRO, toxico);
                break;
            default:
                System.out.println("Tipo inválido.");
                return;
        }

        inv.agregarProducto(producto);
        System.out.println("Producto agregado correctamente.");
    }

    private void listarProductos() {
        System.out.println("\n--- Productos registrados ---");
        ArrayList<Producto> lista = new ArrayList<>(inv.getMapaProductos().values());
        int i = 0;
        while (i < lista.size()) {
            System.out.println(lista.get(i));
            i = i + 1;
        }
    }

    private void agregarComprador() {
        System.out.print("Ingrese RUT del comprador: ");
        String rut = scanner.nextLine().intern();
        System.out.print("Ingrese alias/nombre: ");
        String alias = scanner.nextLine();
        clientes.add(new Comprador(rut, alias));
        System.out.println("Comprador agregado correctamente.");
    }

    private void listarCompradores() {
        System.out.println("\n--- Compradores registrados ---");
        int i = 0;
        while (i < clientes.size()) {
            System.out.println(clientes.get(i));
            i = i + 1;
        }
    }

    private void realizarVenta() {
        System.out.print("Ingrese RUT del comprador: ");
        String rut = scanner.nextLine().intern();

        Comprador comprador = null;
        boolean bandera = false;
        int i = 0;
        while (i < clientes.size() && !bandera) {
            if (clientes.get(i).getRutComprador() == rut) {
                comprador = clientes.get(i);
                bandera = true;
            }
            i = i + 1;
        }

        if (comprador == null) {
            System.out.println("Error: No se encontro comprador con RUT: " + rut);
            return;
        }

        System.out.print("Ingrese ID del producto: ");
        String idProducto = scanner.nextLine().intern();
        Producto producto = inv.buscarProductoPorId(idProducto);

        if (producto == null) {
            System.out.println("Error: El producto con ID " + idProducto + " no existe en el inventario.");
            return;
        }

        System.out.print("Ingrese cantidad: ");
        int cantidad = scanner.nextInt();
        scanner.nextLine();

        if (cantidad > producto.getStock()) {
            System.out.println("Error: Stock insuficiente. Disponible: " + producto.getStock());
            return;
        }

        Boleta boleta = new Boleta("B" + (boletas.size() + 1), comprador, CategoriaOferta.REBAJA_PORCENTUAL);
        boleta.agregarProducto(producto, cantidad);
        boleta.procesarPago();
        boletas.add(boleta);

        System.out.println("Venta realizada exitosamente.");
    }

    private void verBoletas() {
        System.out.println("\n--- Boletas registradas ---");
        if (boletas.isEmpty()) {
            System.out.println("No hay boletas registradas.");
        }
        int i = 0;
        while (i < boletas.size()) {
            System.out.println(boletas.get(i));
            i = i + 1;
        }
    }

    // Getters y Setters
    public Inventario getInventarioTienda() { return inv; }
    public void setInventarioTienda(Inventario inv) { this.inv = inv; }

    public ArrayList<Comprador> getListaCompradores() { return clientes; }
    public void setListaCompradores(ArrayList<Comprador> clientes) { this.clientes = clientes; }

    public ArrayList<Boleta> getRegistroBoletas() { return boletas; }
    public void setRegistroBoletas(ArrayList<Boleta> boletas) { this.boletas = boletas; }
}
