package supermercado.ui;

import supermercado.enums.CategoriaOferta;
import supermercado.enums.Medida;
import supermercado.excepciones.DatoInvalidoException;
import supermercado.excepciones.StockInsuficienteException;
import supermercado.modelo.Boleta;
import supermercado.modelo.Comprador;
import supermercado.modelo.Lacteo;
import supermercado.modelo.Limpieza;
import supermercado.modelo.Producto;
import supermercado.modelo.Verdura;
import supermercado.sistema.ControladorSupermercado;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class ServicioSupermercadoUI {

    private final ControladorSupermercado controlador;

    public ServicioSupermercadoUI(ControladorSupermercado controlador) {
        this.controlador = controlador;
    }

    public ArrayList<Producto> productos() {
        return new ArrayList<>(controlador.getInventarioTienda().getMapaProductos().values());
    }

    public ArrayList<Comprador> compradores() {
        return new ArrayList<>(controlador.getListaCompradores());
    }

    public ArrayList<Boleta> boletas() {
        return new ArrayList<>(controlador.getRegistroBoletas());
    }

    public void agregarProducto(String nombre, double precio, int stock, Medida medida,
                                String tipo, boolean atributo) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new DatoInvalidoException("El nombre del producto es obligatorio.");
        }
        if (!Double.isFinite(precio) || precio < 1) {
            throw new DatoInvalidoException("El precio debe ser un número finito mayor o igual a $1.");
        }
        if (stock < 0 || medida == null) {
            throw new DatoInvalidoException("Revise el stock y la unidad de medida.");
        }

        String id = ControladorSupermercado.generarIdProducto();
        Producto producto;
        switch (tipo) {
            case "Lácteo":
                producto = new Lacteo(id, nombre.trim(), precio, stock, medida, atributo);
                break;
            case "Verdura":
                producto = new Verdura(id, nombre.trim(), precio, stock, medida, atributo);
                break;
            default:
                producto = new Limpieza(id, nombre.trim(), precio, stock, medida, atributo);
                break;
        }
        controlador.getInventarioTienda().agregarProducto(producto);
        guardar();
    }

    public void agregarComprador(String rut, String alias) {
        if (rut == null || rut.trim().isEmpty() || alias == null || alias.trim().isEmpty()) {
            throw new DatoInvalidoException("RUT y nombre del comprador son obligatorios.");
        }
        for (Comprador existente : controlador.getListaCompradores()) {
            if (existente.getRutComprador().equalsIgnoreCase(rut.trim())) {
                throw new DatoInvalidoException("Ya existe un comprador con ese RUT.");
            }
        }
        Comprador comprador = new Comprador(ControladorSupermercado.generarIdComprador(),
                rut.trim(), alias.trim());
        controlador.getListaCompradores().add(comprador);
        guardar();
    }

    public Boleta registrarVenta(Comprador comprador, Producto producto, int cantidad,
                                 CategoriaOferta oferta) {
        if (comprador == null || producto == null || oferta == null) {
            throw new DatoInvalidoException("Seleccione comprador, producto y oferta.");
        }
        if (cantidad <= 0) {
            throw new DatoInvalidoException("La cantidad debe ser mayor que cero.");
        }
        if (cantidad > producto.getStock()) {
            throw new StockInsuficienteException(producto.getStock(), cantidad);
        }

        Boleta boleta = new Boleta(ControladorSupermercado.generarIdBoleta(), comprador, oferta);
        boleta.agregarProducto(producto, cantidad);
        boleta.procesarPago();
        controlador.getRegistroBoletas().add(boleta);
        guardar();
        return boleta;
    }

    public void exportarInventarioJson(File destino) throws IOException {
        ArrayList<Map<String, Object>> filas = new ArrayList<>();
        for (Producto producto : productos()) {
            Map<String, Object> fila = new LinkedHashMap<>();
            fila.put("id", producto.getIdProducto());
            fila.put("nombre", producto.getDenominacion());
            fila.put("tipo", producto.getClass().getSimpleName());
            fila.put("precio", producto.calcularPrecio());
            fila.put("stock", producto.getStock());
            fila.put("unidad", producto.getUnidad().toString());
            fila.put("vendidos", producto.getVendidos());
            filas.add(fila);
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (OutputStreamWriter escritor = new OutputStreamWriter(
                new FileOutputStream(destino), StandardCharsets.UTF_8)) {
            gson.toJson(filas, escritor);
        }
    }

    public void guardar() {
        controlador.getGuardador().guardarDatos();
    }
}