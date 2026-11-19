package supermercado.ui.vistas;

import supermercado.enums.CategoriaOferta;
import supermercado.excepciones.DatoInvalidoException;
import supermercado.excepciones.StockInsuficienteException;
import supermercado.modelo.Boleta;
import supermercado.modelo.Comprador;
import supermercado.modelo.Producto;
import supermercado.ui.Estilo;
import supermercado.ui.ServicioSupermercadoUI;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
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
        JPanel cuerpo = new JPanel();
        cuerpo.setLayout(new BoxLayout(cuerpo, BoxLayout.Y_AXIS));
        cuerpo.setOpaque(false);
        JPanel formulario = tarjeta();
        formulario.setPreferredSize(new Dimension(850, 180));
        formulario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        JPanel campos = new JPanel(new GridLayout(2, 1, 0, 12));
        campos.setOpaque(false);

        JPanel filaPrincipal = new JPanel(new GridLayout(1, 2, 14, 0));
        filaPrincipal.setOpaque(false);
        filaPrincipal.add(campo("Comprador", comprador));
        filaPrincipal.add(campo("Producto", producto));

        JPanel filaOpciones = new JPanel(new BorderLayout(14, 0));
        filaOpciones.setOpaque(false);
        JPanel opciones = new JPanel(new GridLayout(1, 2, 14, 0));
        opciones.setOpaque(false);
        opciones.add(campo("Cantidad", cantidad));
        opciones.add(campo("Oferta", oferta));
        filaOpciones.add(opciones, BorderLayout.CENTER);
        campos.add(filaPrincipal);
        campos.add(filaOpciones);
        formulario.add(campos, BorderLayout.CENTER);

        JPanel historial = tarjeta();
        historial.setLayout(new BorderLayout(0, 8));
        historial.setPreferredSize(new Dimension(850, 260));
        historial.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));
        JLabel titulo = new JLabel("Historial de boletas");
        titulo.setFont(Estilo.FUENTE_TARJETA);
        titulo.setForeground(Estilo.AZUL);
        JTable tabla = new JTable(modelo);
        Estilo.tabla(tabla);
        JScrollPane desplazamiento = new JScrollPane(tabla);
        desplazamiento.setPreferredSize(new Dimension(850, 205));
        desplazamiento.setMaximumSize(new Dimension(Integer.MAX_VALUE, 205));
        historial.add(titulo, BorderLayout.NORTH);
        historial.add(desplazamiento, BorderLayout.CENTER);

        JButton registrar = Estilo.boton("Comprar y crear boleta", Estilo.TURQUESA);
        registrar.setPreferredSize(new Dimension(245, 48));
        registrar.setMinimumSize(new Dimension(245, 48));
        JPanel pie = new JPanel(new BorderLayout(12, 0));
        pie.setOpaque(false);
        pie.setPreferredSize(new Dimension(850, 64));
        pie.add(registrar, BorderLayout.EAST);

        cuerpo.add(formulario);
        cuerpo.add(Box.createVerticalStrut(12));
        cuerpo.add(historial);
        add(cuerpo, BorderLayout.CENTER);
        add(pie, BorderLayout.SOUTH);
        registrar.addActionListener(e -> registrar());
    }

    private JPanel campo(String titulo, JComponent componente) {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setOpaque(false);
        JLabel etiqueta = new JLabel(titulo);
        etiqueta.setForeground(Estilo.TEXTO);
        panel.add(etiqueta, BorderLayout.NORTH);
        panel.add(componente, BorderLayout.CENTER);
        return panel;
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