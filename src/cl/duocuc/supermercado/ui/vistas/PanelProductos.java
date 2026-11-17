package cl.duocuc.supermercado.ui.vistas;

import cl.duocuc.supermercado.enums.Medida;
import cl.duocuc.supermercado.excepciones.DatoInvalidoException;
import cl.duocuc.supermercado.modelo.Producto;
import cl.duocuc.supermercado.ui.EstilosUI;
import cl.duocuc.supermercado.ui.ServicioSupermercadoUI;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings({"serial", "this-escape"})
public class PanelProductos extends PanelBase {

    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"ID", "Producto", "Tipo", "Precio final", "Stock", "Unidad", "Vendidos"}, 0) {
        @Override public boolean isCellEditable(int fila, int columna) { return false; }
    };
    private final JTextField buscar = new JTextField(18);

    public PanelProductos(ServicioSupermercadoUI servicio) {
        super(servicio);
        add(encabezado("Productos", "Administra el catálogo y consulta las existencias."), BorderLayout.NORTH);
        JPanel cuerpo = tarjeta();
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        barra.setOpaque(false);
        buscar.putClientProperty("JTextField.placeholderText", "Buscar por nombre o ID");
        JButton filtrar = EstilosUI.boton("Buscar", EstilosUI.AZUL);
        JButton nuevo = EstilosUI.boton("＋ Nuevo producto", EstilosUI.TURQUESA);
        JButton exportar = EstilosUI.boton("Exportar JSON", EstilosUI.BLANCO);
        barra.add(new JLabel("Filtrar"));
        barra.add(buscar);
        barra.add(filtrar);
        barra.add(nuevo);
        barra.add(exportar);
        JTable tabla = new JTable(modelo);
        EstilosUI.tabla(tabla);
        cuerpo.add(barra, BorderLayout.NORTH);
        cuerpo.add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(cuerpo, BorderLayout.CENTER);
        filtrar.addActionListener(e -> actualizar());
        buscar.addActionListener(e -> actualizar());
        nuevo.addActionListener(e -> agregar());
        exportar.addActionListener(e -> exportar());
    }

    public void actualizar() {
        modelo.setRowCount(0);
        String criterio = buscar.getText().trim().toLowerCase();
        for (Producto producto : servicio.productos()) {
            if (!criterio.isEmpty()
                    && !producto.getDenominacion().toLowerCase().contains(criterio)
                    && !producto.getIdProducto().toLowerCase().contains(criterio)) continue;
            modelo.addRow(new Object[]{producto.getIdProducto(), producto.getDenominacion(),
                producto.getClass().getSimpleName(), String.format("$%,.0f", producto.calcularPrecio()),
                producto.getStock(), producto.getUnidad(), producto.getVendidos()});
        }
    }

    private void agregar() {
        JTextField nombre = new JTextField(18);
        JTextField precio = new JTextField(18);
        JSpinner stock = new JSpinner(new SpinnerNumberModel(1, 0, 100000, 1));
        JComboBox<String> tipo = new JComboBox<>(new String[]{"Lácteo", "Verdura", "Limpieza"});
        JComboBox<Medida> unidad = new JComboBox<>(Medida.values());
        JCheckBox atributo = new JCheckBox("Sí");
        JLabel etiqueta = new JLabel("Requiere frío");
        tipo.addActionListener(e -> etiqueta.setText(tipo.getSelectedItem().equals("Lácteo")
                ? "Requiere frío" : tipo.getSelectedItem().equals("Verdura") ? "Es orgánica" : "Es tóxico"));
        JPanel formulario = new JPanel(new java.awt.GridLayout(0, 2, 8, 8));
        formulario.add(new JLabel("Nombre")); formulario.add(nombre);
        formulario.add(new JLabel("Precio base")); formulario.add(precio);
        formulario.add(new JLabel("Stock inicial")); formulario.add(stock);
        formulario.add(new JLabel("Categoría")); formulario.add(tipo);
        formulario.add(new JLabel("Unidad")); formulario.add(unidad);
        formulario.add(etiqueta); formulario.add(atributo);
        int resultado = JOptionPane.showConfirmDialog(this, formulario, "Nuevo producto",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (resultado != JOptionPane.OK_OPTION) return;
        try {
            double valor = Double.parseDouble(precio.getText().trim().replace(',', '.'));
            servicio.agregarProducto(nombre.getText(), valor, (Integer) stock.getValue(),
                    (Medida) unidad.getSelectedItem(), (String) tipo.getSelectedItem(), atributo.isSelected());
            actualizar();
            JOptionPane.showMessageDialog(this, "Producto agregado y guardado.", "Catálogo actualizado",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException | DatoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo agregar",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void exportar() {
        JFileChooser selector = new JFileChooser();
        selector.setSelectedFile(new File("inventario-supermercado.json"));
        if (selector.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        File archivo = selector.getSelectedFile();
        if (!archivo.getName().toLowerCase().endsWith(".json")) {
            archivo = new File(archivo.getParentFile(), archivo.getName() + ".json");
        }
        try {
            servicio.exportarInventarioJson(archivo);
            JOptionPane.showMessageDialog(this, "Inventario exportado a:\n" + archivo.getAbsolutePath(),
                    "Exportación lista", JOptionPane.INFORMATION_MESSAGE);
        } catch (java.io.IOException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo exportar: " + ex.getMessage(),
                    "Error de exportación", JOptionPane.ERROR_MESSAGE);
        }
    }
}
