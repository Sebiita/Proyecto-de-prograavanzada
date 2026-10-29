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
import java.util.Comparator;
import java.util.Scanner;

//REQUISITO: Colecciones
//REQUISITO: Menu - Scanner

public class ControladorSupermercado {

    //REQUISITO: Static y Final
    private static final String NOMBRE_SISTEMA = "SUPERMERCADO";
    private static final double PRECIO_MINIMO_PRODUCTO = 1.0;
    private static final String PREFIJO_PRODUCTO = "PROD-";
    private static final String PREFIJO_BOLETA = "BOL-";
    private static final String PREFIJO_COMPRADOR = "CLI-";

    //REQUISITO: Static y Final
    private static int contadorProductos = 1;
    private static int contadorBoletas = 1;
    private static int contadorCompradores = 1;

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
    }

    public GuardadoSeguro getGuardador() {
        if (guardador == null) {
            guardador = new GuardadoSeguro(this);
        }
        return guardador;
    }

    //REQUISITO: Static y Final
    public static String generarIdProducto() {
        return PREFIJO_PRODUCTO + String.format("%03d", contadorProductos++);
    }

    //REQUISITO: Static y Final
    public static String generarIdBoleta() {
        return PREFIJO_BOLETA + String.format("%03d", contadorBoletas++);
    }

    //REQUISITO: Static y Final
    public static String generarIdComprador() {
        return PREFIJO_COMPRADOR + String.format("%03d", contadorCompradores++);
    }

    //REQUISITO: Excepciones
    //Lectura robusta de enteros usando nextLine() para evitar datos residuales
    private int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Error: debe ingresar un número entero válido.");
            }
        }
    }

    //REQUISITO: Excepciones
    //Lectura robusta de decimales usando nextLine() para evitar datos residuales
    private double leerDouble(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Error: debe ingresar un número decimal válido.");
            }
        }
    }

    //evita duplicar la búsqueda de comprador por RUT
    private Comprador buscarCompradorPorRut(String rut) {
        int i = 0;
        while (i < clientes.size()) {
            if (clientes.get(i).getRutComprador().equals(rut)) {
                return clientes.get(i);
            }
            i = i + 1;
        }
        return null;
    }

    //REQUISITO: Static y Final
    //Getters y setters para que GuardadoSeguro persista los contadores en un archivo de texto
    public static int getContadorProductos() {
        return contadorProductos;
    }

    public static void setContadorProductos(int contadorProductos) {
        ControladorSupermercado.contadorProductos = contadorProductos;
    }

    public static int getContadorBoletas() {
        return contadorBoletas;
    }

    public static void setContadorBoletas(int contadorBoletas) {
        ControladorSupermercado.contadorBoletas = contadorBoletas;
    }

    public static int getContadorCompradores() {
        return contadorCompradores;
    }

    public static void setContadorCompradores(int contadorCompradores) {
        ControladorSupermercado.contadorCompradores = contadorCompradores;
    }

    public void precargarDatos() {
        //REQUISITO: Colecciones
        //REQUISITO: Herencia
        //instancias de cada subtipo de producto
        Producto p1 = new Lacteo(generarIdProducto(), "Leche Entera", 1200, 20, Medida.LITRO, true);
        Producto p2 = new Lacteo(generarIdProducto(), "Yogur Natural", 800, 15, Medida.UNIDAD, true);
        Producto p7 = new Lacteo(generarIdProducto(), "Queso Fresco", 3500, 10, Medida.KILOGRAMO, true);
        Producto p3 = new Verdura(generarIdProducto(), "Zanahoria", 500, 30, Medida.KILOGRAMO, false);
        Producto p4 = new Verdura(generarIdProducto(), "Manzana Organica", 1200, 25, Medida.KILOGRAMO, true);
        Producto p8 = new Verdura(generarIdProducto(), "Lechuga", 900, 40, Medida.UNIDAD, true);
        Producto p5 = new Limpieza(generarIdProducto(), "Detergente", 2500, 10, Medida.LITRO, false);
        Producto p6 = new Limpieza(generarIdProducto(), "Cloro", 1500, 12, Medida.LITRO, true);
        Producto p9 = new Limpieza(generarIdProducto(), "Jabon Liquido", 1800, 18, Medida.LITRO, false);

        inv.agregarProducto(p1);
        inv.agregarProducto(p2);
        inv.agregarProducto(p3);
        inv.agregarProducto(p4);
        inv.agregarProducto(p5);
        inv.agregarProducto(p6);
        inv.agregarProducto(p7);
        inv.agregarProducto(p8);
        inv.agregarProducto(p9);

        //clientes
        Comprador c1 = new Comprador(generarIdComprador(), "12345678-9", "Seba ruiz");
        Comprador c2 = new Comprador(generarIdComprador(), "98765432-1", "Cristian ruiz");
        Comprador c3 = new Comprador(generarIdComprador(), "11111111-1", "Santiago Farfan");
        Comprador c4 = new Comprador(generarIdComprador(), "22222222-2", "Maria Gonzalez");
        Comprador c5 = new Comprador(generarIdComprador(), "33333333-3", "Pedro Soto");
        clientes.add(c1);
        clientes.add(c2);
        clientes.add(c3);
        clientes.add(c4);
        clientes.add(c5);

        //operaciones registradas en el historial
        crearBoletaPrecargada(c1, CategoriaOferta.REBAJA_PORCENTUAL, p1, 2);
        crearBoletaPrecargada(c2, CategoriaOferta.DOS_POR_UNO, p3, 4);
        crearBoletaPrecargada(c3, CategoriaOferta.REBAJA_PORCENTUAL, p5, 1);
        crearBoletaPrecargada(c1, CategoriaOferta.DOS_POR_UNO, p6, 2);
        crearBoletaPrecargada(c4, CategoriaOferta.REBAJA_PORCENTUAL, p2, 3);
        crearBoletaPrecargada(c5, CategoriaOferta.REBAJA_PORCENTUAL, p4, 2);
        crearBoletaPrecargada(c2, CategoriaOferta.DOS_POR_UNO, p8, 6);
        crearBoletaPrecargada(c3, CategoriaOferta.REBAJA_PORCENTUAL, p9, 2);

        System.out.println("Datos precargados correctamente.");
    }

    private void crearBoletaPrecargada(Comprador comprador,
                                       CategoriaOferta oferta, Producto producto, int cantidad) {
        if (producto != null && producto.getStock() >= cantidad) {
            Boleta boleta = new Boleta(generarIdBoleta(), comprador, oferta);
            boleta.agregarProducto(producto, cantidad);
            boleta.procesarPago();
            boletas.add(boleta);
        }
    }

    public void iniciarMenu() {
        //REQUISITO: Menu - Scanner
        if (scanner == null) {
            scanner = new Scanner(System.in);
        }

        int opcion;
        do {
            System.out.println("\n===== " + NOMBRE_SISTEMA + " =====");
            System.out.println("1. Agregar producto");
            System.out.println("2. Listar productos");
            System.out.println("3. Buscar producto");
            System.out.println("4. Agregar comprador");
            System.out.println("5. Listar compradores");
            System.out.println("6. Ver perfil de comprador");
            System.out.println("7. Realizar venta");
            System.out.println("8. Ver boletas");
            System.out.println("9. Ranking de productos");
            System.out.println("10. Ranking de compradores");
            System.out.println("11. Ordenar productos");
            System.out.println("12. Salir");
            opcion = leerEntero("Seleccione una opción: ");

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
                        rankingProductos();
                        break;
                    case 10:
                        rankingCompradores();
                        break;
                    case 11:
                        ordenarProductos();
                        break;
                    case 12:
                        //REQUISITO: Persistencia
                        getGuardador().guardarDatos();
                        System.out.println("Saliendo del sistema...");
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (CompradorNoEncontradoException | ProductoNoEncontradoException
                     | StockInsuficienteException | DatoInvalidoException e) {
                //REQUISITO: Excepciones
                System.out.println("Error: " + e.getMessage());
            }
        } while (opcion != 12);
    }

    private void agregarProducto() {
        //REQUISITO: Static y Final
        String id = generarIdProducto();
        System.out.println("ID asignado automáticamente: " + id);
        System.out.print("Ingrese denominación: ");
        String denominacion = scanner.nextLine();
        double valorBase = leerDouble("Ingrese valor base: ");
        //REQUISITO: Static y Final
        if (valorBase < PRECIO_MINIMO_PRODUCTO) {
            throw new DatoInvalidoException("El valor base debe ser al menos $" + PRECIO_MINIMO_PRODUCTO);
        }
        int cantidad = leerEntero("Ingrese cantidad disponible: ");

        System.out.println("Seleccione tipo: 1. Lácteo 2. Verdura 3. Limpieza");
        int tipo = leerEntero("Seleccione tipo: ");

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
        //REQUISITO: Static y Final
        String id = generarIdComprador();
        System.out.println("ID asignado automáticamente: " + id);
        System.out.print("Ingrese RUT del comprador: ");
        String rut = scanner.nextLine();
        System.out.print("Ingrese alias/nombre: ");
        String alias = scanner.nextLine();
        clientes.add(new Comprador(id, rut, alias));
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

        Comprador comprador = buscarCompradorPorRut(rut);
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
        int i = 0;
        while (i < historial.size()) {
            System.out.println(historial.get(i));
            i = i + 1;
        }
    }

    private void realizarVenta() {
        System.out.print("Ingrese RUT del comprador: ");
        String rut = scanner.nextLine();

        Comprador comprador = buscarCompradorPorRut(rut);
        if (comprador == null) {
            //REQUISITO: Excepciones
            throw new CompradorNoEncontradoException("No se encontró comprador con RUT: " + rut);
        }

        System.out.print("Ingrese ID del producto: ");
        String idProducto = scanner.nextLine();
        Producto producto = inv.buscarProductoPorId(idProducto);

        if (producto == null) {
            //REQUISITO: Excepciones
            throw new ProductoNoEncontradoException("El producto con ID " + idProducto + " no existe en el inventario.");
        }

        int cantidad = leerEntero("Ingrese cantidad: ");

        if (cantidad > producto.getStock()) {
            //REQUISITO: Excepciones
            throw new StockInsuficienteException(producto.getStock(), cantidad);
        }
        Boleta boleta = new Boleta(generarIdBoleta(), comprador, CategoriaOferta.REBAJA_PORCENTUAL);
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
            Boleta b = boletas.get(i);
            System.out.println(b + " | Formateado: " + formatearPrecio(b.getTotalPagar()));
            i = i + 1;
        }
    }

    //REQUISITO: Static
    public static String formatearPrecio(double precio) {
        return "$" + String.format("%.2f", precio);
    }

    //REQUISITO: Colecciones
    //REQUISITO: Interface
    private void rankingProductos() {
        System.out.println("\n--- Ranking de productos más vendidos ---");
        ArrayList<Producto> lista = new ArrayList<>(inv.getMapaProductos().values());
        lista.sort((p1, p2) -> Double.compare(p2.obtenerCriterioRanking(), p1.obtenerCriterioRanking()));

        int posicion = 1;
        int i = 0;
        while (i < lista.size()) {
            Producto p = lista.get(i);
            System.out.println(posicion + ". " + p.getDenominacion()
                    + " | Vendidos: " + p.getVendidos()
                    + " | Ingresos: " + formatearPrecio(p.obtenerCriterioRanking()));
            posicion = posicion + 1;
            i = i + 1;
        }
    }

    // REQUISITO: Colecciones
    //REQUISITO: Interface
    private void rankingCompradores() {
        System.out.println("\n--- Ranking de compradores ---");
        ArrayList<Comprador> lista = new ArrayList<>(clientes);
        lista.sort((c1, c2) -> Double.compare(c2.obtenerCriterioRanking(), c1.obtenerCriterioRanking()));

        int posicion = 1;
        int i = 0;
        while (i < lista.size()) {
            Comprador c = lista.get(i);
            System.out.println(posicion + ". " + c.getAlias()
                    + " | Total gastado: " + formatearPrecio(c.obtenerCriterioRanking()));
            posicion = posicion + 1;
            i = i + 1;
        }
    }

    //REQUISITO: Colecciones
    private void ordenarProductos() {
        System.out.println("\n--- Ordenar productos ---");
        System.out.println("1. Por nombre");
        System.out.println("2. Por precio final");
        System.out.println("3. Por stock");
        System.out.println("4. Por tipo");
        int criterio = leerEntero("Seleccione criterio: ");

        ArrayList<Producto> lista = new ArrayList<>(inv.getMapaProductos().values());
        switch (criterio) {
            case 1:
                lista.sort(Comparator.comparing(p -> p.getDenominacion().toLowerCase()));
                break;
            case 2:
                lista.sort(Comparator.comparingDouble(Producto::calcularPrecio));
                break;
            case 3:
                lista.sort(Comparator.comparingInt(Producto::getStock));
                break;
            case 4:
                lista.sort(Comparator.comparing(this::obtenerTipoProducto));
                break;
            default:
                System.out.println("Criterio no válido.");
                return;
        }

        System.out.println("\n--- Productos ordenados ---");
        int i = 0;
        while (i < lista.size()) {
            Producto p = lista.get(i);
            System.out.println("[" + obtenerTipoProducto(p) + "] " + p);
            i = i + 1;
        }
    }

    //REQUISITO: Herencia
    private String obtenerTipoProducto(Producto p) {
        if (p instanceof Lacteo) {
            return "Lacteo";
        } else if (p instanceof Verdura) {
            return "Verdura";
        } else if (p instanceof Limpieza) {
            return "Limpieza";
        }
        return "Desconocido";
    }

    //Getters y Setters
    public Inventario getInventarioTienda() { return inv; }
    public void setInventarioTienda(Inventario inv) { this.inv = inv; }

    public ArrayList<Comprador> getListaCompradores() { return clientes; }
    public void setListaCompradores(ArrayList<Comprador> clientes) { this.clientes = clientes; }

    public ArrayList<Boleta> getRegistroBoletas() { return boletas; }
    public void setRegistroBoletas(ArrayList<Boleta> boletas) { this.boletas = boletas; }
}