package cl.duocuc.supermercado.ui.vistas;

import cl.duocuc.supermercado.enums.CategoriaOferta;
import cl.duocuc.supermercado.excepciones.DatoInvalidoException;
import cl.duocuc.supermercado.excepciones.StockInsuficienteException;
import cl.duocuc.supermercado.modelo.Boleta;
import cl.duocuc.supermercado.modelo.Comprador;
import cl.duocuc.supermercado.modelo.Producto;
import cl.duocuc.supermercado.ui.EstilosUI;
import cl.duocuc.supermercado.ui.ServicioSupermercadoUI;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings({"serial", "this-escape"})
public class PanelVentas extends PanelBase {

    private final JComboBox<Comprador> comprador = new JComboBox<>();
    private final JComboBox<Producto> producto = new JComboBox<>();
    private final JComboBox<CategoriaOferta> oferta = new JComboBox<>(CategoriaOferta.values());
    private final JSpinner cantidad = new JSpinner(new SpinnerNumberModel(1, 1, 100000, 1));
    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Boleta", "Comprador", "Artículos", "Total", "Oferta"}, 0) {
        @Override public boolean isCellEditable(int fila, int columna) { return false; }
    };

    public PanelVentas(ServicioSupermercadoUI servicio) {
        super(servicio);
        add(encabezado("Ventas y boletas", "Registra una compra y actualiza el inventario al confirmar."), BorderLayout.NORTH);
        JPanel cuerpo = new JPanel(new BorderLayout(0, 14));
        cuerpo.setOpaque(false);
        JPanel formulario = tarjeta();
        JPanel campos = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 4));
        campos.setOpaque(false);
        campos.add(new JLabel("Comprador")); campos.add(comprador);
        campos.add(new JLabel("Producto")); campos.add(producto);
        campos.add(new JLabel("Cantidad")); campos.add(cantidad);
        campos.add(new JLabel("Oferta")); campos.add(oferta);
        JButton registrar = EstilosUI.boton("Registrar venta", EstilosUI.TURQUESA);
        campos.add(registrar);
        formulario.add(campos, BorderLayout.CENTER);

        JPanel historial = tarjeta();
        JLabel titulo = new JLabel("Historial de boletas");
        titulo.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 16));
        titulo.setForeground(EstilosUI.AZUL);
        JTable tabla = new JTable(modelo);
        EstilosUI.tabla(tabla);
        historial.add(titulo, BorderLayout.NORTH);
        historial.add(new JScrollPane(tabla), BorderLayout.CENTER);
        cuerpo.add(formulario, BorderLayout.NORTH);
        cuerpo.add(historial, BorderLayout.CENTER);
        add(cuerpo, BorderLayout.CENTER);
        registrar.addActionListener(e -> registrar());
    }

    public void actualizar() {
        Object clienteActual = comprador.getSelectedItem();
        Object productoActual = producto.getSelectedItem();
        comprador.removeAllItems();
        for (Comprador cliente : servicio.compradores()) comprador.addItem(cliente);
        producto.removeAllItems();
        for (Producto articulo : servicio.productos()) producto.addItem(articulo);
        if (clienteActual != null) comprador.setSelectedItem(clienteActual);
        if (productoActual != null) producto.setSelectedItem(productoActual);
        comprador.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> lista,
                    Object valor, int indice, boolean seleccionado, boolean enfocado) {
                super.getListCellRendererComponent(lista, valor, indice, seleccionado, enfocado);
                if (valor instanceof Comprador c) setText(c.getAlias() + " · " + c.getRutComprador());
                return this;
            }
        });
        producto.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> lista,
                    Object valor, int indice, boolean seleccionado, boolean enfocado) {
                super.getListCellRendererComponent(lista, valor, indice, seleccionado, enfocado);
                if (valor instanceof Producto p) setText(p.getDenominacion() + " · " + p.getStock() + " disponibles");
                return this;
            }
        });
        modelo.setRowCount(0);
        java.util.List<Boleta> lista = servicio.boletas();
        for (int i = lista.size() - 1; i >= 0; i--) {
            Boleta boleta = lista.get(i);
            modelo.addRow(new Object[]{boleta.getIdBoleta(),
                boleta.getComprador() == null ? "Sin comprador" : boleta.getComprador().getAlias(),
                boleta.getListaCompra().size(), String.format("$%,.0f", boleta.getTotalPagar()),
                boleta.getCategoriaOferta()});
        }
    }

    private void registrar() {
        Comprador cliente = (Comprador) comprador.getSelectedItem();
        Producto articulo = (Producto) producto.getSelectedItem();
        if (cliente == null || articulo == null) {
            JOptionPane.showMessageDialog(this, "Primero debe registrar al menos un comprador y un producto.",
                    "Datos insuficientes", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int unidades = (Integer) cantidad.getValue();
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "Registrar " + unidades + " × " + articulo.getDenominacion() + " para "
                + cliente.getAlias() + "?", "Confirmar venta", JOptionPane.YES_NO_OPTION);
        if (confirmacion != JOptionPane.YES_OPTION) return;
        try {
            Boleta boleta = servicio.registrarVenta(cliente, articulo, unidades,
                    (CategoriaOferta) oferta.getSelectedItem());
            actualizar();
            JOptionPane.showMessageDialog(this, "Boleta " + boleta.getIdBoleta()
                    + " registrada. Total: " + String.format("$%,.0f", boleta.getTotalPagar()),
                    "Venta realizada", JOptionPane.INFORMATION_MESSAGE);
        } catch (DatoInvalidoException | StockInsuficienteException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo registrar la venta",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}
