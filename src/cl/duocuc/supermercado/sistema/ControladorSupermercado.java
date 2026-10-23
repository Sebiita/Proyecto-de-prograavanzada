package cl.duocuc.supermercado.sistema;

import cl.duocuc.supermercado.enums.CategoriaOferta;
import cl.duocuc.supermercado.enums.Medida;
import cl.duocuc.supermercado.excepciones.CompradorNoEncontradoException;
import cl.duocuc.supermercado.excepciones.DatoInvalidoException;
import cl.duocuc.supermercado.excepciones.ProductoNoEncontradoException;
import cl.duocuc.supermercado.excepciones.StockInsuficienteException;
import cl.duocuc.supermercado.modelo.Boleta;
import cl.duocuc.supermercado.modelo.Comprador;
import cl.duocuc.supermercado.modelo.Inventario;
import cl.duocuc.supermercado.modelo.Lacteo;
import cl.duocuc.supermercado.modelo.Limpieza;
import cl.duocuc.supermercado.modelo.Producto;
import cl.duocuc.supermercado.modelo.Verdura;
import java.util.ArrayList;
import java.util.Scanner;

// [REQUISITO: Package]
// [REQUISITO: Colecciones]
// [REQUISITO: Menu - Scanner]

public class ControladorSupermercado {

    // [REQUISITO: Static y Final]
    private static final String NOMBRE_SISTEMA = "SUPERMERCADO";
    private static final double PRECIO_MINIMO_PRODUCTO = 1.0;

    private Inventario inv;
    private ArrayList<Comprador> clientes;
    private ArrayList<Boleta> boletas;

    private transient Scanner scanner;
    private GuardadoSeguro guardador;

    public ControladorSupermercado() {
        this.inv = new Inventario();
        this.clientes = new ArrayList<>();
        this.boletas = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        this.guardador = new GuardadoSeguro(this);
    }

    public void precargarDatos() {
        // [REQUISITO: Colecciones]
        // [REQUISITO: Herencia]
        // Mínimo 3 instancias de cada subtipo de producto
        inv.agregarProducto(new Lacteo("P001", "Leche Entera", 1200, 20, Medida.LITRO, true));
        inv.agregarProducto(new Lacteo("P002", "Yogur Natural", 800, 15, Medida.UNIDAD, true));
        inv.agregarProducto(new Lacteo("P007", "Queso Fresco", 3500, 10, Medida.KILOGRAMO, true));
        inv.agregarProducto(new Verdura("P003", "Zanahoria", 500, 30, Medida.KILOGRAMO, false));
        inv.agregarProducto(new Verdura("P004", "Manzana Organica", 1200, 25, Medida.KILOGRAMO, true));
        inv.agregarProducto(new Verdura("P008", "Lechuga", 900, 40, Medida.UNIDAD, true));
        inv.agregarProducto(new Limpieza("P005", "Detergente", 2500, 10, Medida.LITRO, false));
        inv.agregarProducto(new Limpieza("P006", "Cloro", 1500, 12, Medida.LITRO, true));
        inv.agregarProducto(new Limpieza("P009", "Jabon Liquido", 1800, 18, Medida.LITRO, false));

        // Al menos 5 clientes
        Comprador c1 = new Comprador("12345678-9", "Seba ruiz");
        Comprador c2 = new Comprador("98765432-1", "Cristian ruiz");
        Comprador c3 = new Comprador("11111111-1", "Santiago Farfan");
        Comprador c4 = new Comprador("22222222-2", "Maria Gonzalez");
        Comprador c5 = new Comprador("33333333-3", "Pedro Soto");
        clientes.add(c1);
        clientes.add(c2);
        clientes.add(c3);
        clientes.add(c4);
        clientes.add(c5);

        // Al menos 8 operaciones registradas en el historial
        crearBoletaPrecargada("B001", c1, CategoriaOferta.REBAJA_PORCENTUAL, "P001", 2);
        crearBoletaPrecargada("B002", c2, CategoriaOferta.DOS_POR_UNO, "P003", 4);
        crearBoletaPrecargada("B003", c3, CategoriaOferta.REBAJA_PORCENTUAL, "P005", 1);
        crearBoletaPrecargada("B004", c1, CategoriaOferta.DOS_POR_UNO, "P006", 2);
        crearBoletaPrecargada("B005", c4, CategoriaOferta.REBAJA_PORCENTUAL, "P002", 3);
        crearBoletaPrecargada("B006", c5, CategoriaOferta.REBAJA_PORCENTUAL, "P004", 2);
        crearBoletaPrecargada("B007", c2, CategoriaOferta.DOS_POR_UNO, "P008", 6);
        crearBoletaPrecargada("B008", c3, CategoriaOferta.REBAJA_PORCENTUAL, "P009", 2);

        System.out.println("Datos precargados correctamente.");
    }

    private void crearBoletaPrecargada(String id, Comprador comprador,
                                       CategoriaOferta oferta, String idProducto, int cantidad) {
        Producto producto = inv.buscarProductoPorId(idProducto);
        if (producto != null && producto.getStock() >= cantidad) {
            Boleta boleta = new Boleta(id, comprador, oferta);
            boleta.agregarProducto(producto, cantidad);
            boleta.procesarPago();
            boletas.add(boleta);
        }
    }

    public void iniciarMenu() {
        // [REQUISITO: Menu - Scanner]
        if (scanner == null) {
            scanner = new Scanner(System.in);
        }

        int opcion;
        do {
            // [REQUISITO: Static y Final]
            System.out.println("\n===== " + NOMBRE_SISTEMA + " =====");
            System.out.println("1. Agregar producto");
            System.out.println("2. Listar productos");
            System.out.println("3. Buscar producto");
            System.out.println("4. Agregar comprador");
            System.out.println("5. Listar compradores");
            System.out.println("6. Ver perfil de comprador");
            System.out.println("7. Realizar venta");
            System.out.println("8. Ver boletas");
            System.out.println("9. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (opcion) {
                    case 1:
                        agregarProducto();
                        break;
                    case 2:
                        listarProductos();
                        break;
                    case 3:
                        buscarProducto();
                        break;
                    case 4:
                        agregarComprador();
                        break;
                    case 5:
                        listarCompradores();
                        break;
                    case 6:
                        verPerfilComprador();
                        break;
                    case 7:
                        realizarVenta();
                        break;
                    case 8:
                        verBoletas();
                        break;
                    case 9:
                        // [REQUISITO: Persistencia]
                        guardador.guardarDatos();
                        System.out.println("Saliendo del sistema...");
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (CompradorNoEncontradoException | ProductoNoEncontradoException
                     | StockInsuficienteException | DatoInvalidoException e) {
                // [REQUISITO: Excepciones]
                System.out.println("Error: " + e.getMessage());
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
        // [REQUISITO: Static y Final]
        if (valorBase < PRECIO_MINIMO_PRODUCTO) {
            throw new DatoInvalidoException("El valor base debe ser al menos $" + PRECIO_MINIMO_PRODUCTO);
        }
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

    private void buscarProducto() {
        System.out.println("\n--- Buscar producto ---");
        System.out.print("Ingrese ID o nombre del producto: ");
        String criterio = scanner.nextLine().toLowerCase();

        boolean encontrado = false;
        ArrayList<Producto> lista = new ArrayList<>(inv.getMapaProductos().values());
        int i = 0;
        while (i < lista.size()) {
            Producto p = lista.get(i);
            if (p.getIdProducto().toLowerCase().contains(criterio)
                    || p.getDenominacion().toLowerCase().contains(criterio)) {
                System.out.println(p);
                encontrado = true;
            }
            i = i + 1;
        }

        if (!encontrado) {
            throw new ProductoNoEncontradoException("No se encontró producto con ID o nombre: " + criterio);
        }
    }

    private void agregarComprador() {
        System.out.print("Ingrese RUT del comprador: ");
        String rut = scanner.nextLine();
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

    private void verPerfilComprador() {
        System.out.print("Ingrese RUT del comprador: ");
        String rut = scanner.nextLine();

        Comprador comprador = null;
        int i = 0;
        while (i < clientes.size() && comprador == null) {
            if (clientes.get(i).getRutComprador().equals(rut)) {
                comprador = clientes.get(i);
            }
            i = i + 1;
        }

        if (comprador == null) {
            throw new CompradorNoEncontradoException("No se encontró comprador con RUT: " + rut);
        }

        System.out.println("\n--- Perfil del comprador ---");
        System.out.println(comprador);
        System.out.println("--- Historial de boletas ---");
        ArrayList<Boleta> historial = comprador.getHistorialBoletas();
        if (historial.isEmpty()) {
            System.out.println("El comprador no tiene boletas registradas.");
        }
        i = 0;
        while (i < historial.size()) {
            System.out.println(historial.get(i));
            i = i + 1;
        }
    }

    private void realizarVenta() {
        System.out.print("Ingrese RUT del comprador: ");
        String rut = scanner.nextLine();

        Comprador comprador = null;
        boolean bandera = false;
        int i = 0;
        while (i < clientes.size() && !bandera) {
            if (clientes.get(i).getRutComprador().equals(rut)) {
                comprador = clientes.get(i);
                bandera = true;
            }
            i = i + 1;
        }

        if (comprador == null) {
            // [REQUISITO: Excepciones]
            throw new CompradorNoEncontradoException("No se encontró comprador con RUT: " + rut);
        }

        System.out.print("Ingrese ID del producto: ");
        String idProducto = scanner.nextLine();
        Producto producto = inv.buscarProductoPorId(idProducto);

        if (producto == null) {
            // [REQUISITO: Excepciones]
            throw new ProductoNoEncontradoException("El producto con ID " + idProducto + " no existe en el inventario.");
        }

        System.out.print("Ingrese cantidad: ");
        int cantidad = scanner.nextInt();
        scanner.nextLine();

        if (cantidad > producto.getStock()) {
            // [REQUISITO: Excepciones]
            throw new StockInsuficienteException(producto.getStock(), cantidad);
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
            // [REQUISITO: Static y Final]
            System.out.println(boletas.get(i) + " | Formateado: " + formatearPrecio(boletas.get(i).getTotalPagar()));
            i = i + 1;
        }
    }

    // [REQUISITO: Static y Final]
    public static String formatearPrecio(double precio) {
        return "$" + String.format("%.2f", precio);
    }

    // Getters y Setters
    public Inventario getInventarioTienda() { return inv; }
    public void setInventarioTienda(Inventario inv) { this.inv = inv; }

    public ArrayList<Comprador> getListaCompradores() { return clientes; }
    public void setListaCompradores(ArrayList<Comprador> clientes) { this.clientes = clientes; }

    public ArrayList<Boleta> getRegistroBoletas() { return boletas; }
    public void setRegistroBoletas(ArrayList<Boleta> boletas) { this.boletas = boletas; }

    public GuardadoSeguro getGuardador() { return guardador; }
}
