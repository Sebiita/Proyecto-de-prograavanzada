package supermercado.ui.vistas;

import supermercado.excepciones.DatoInvalidoException;
import supermercado.modelo.Comprador;
import supermercado.ui.Estilo;
import supermercado.ui.ServicioSupermercadoUI;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings({"serial", "this-escape"})
public class PanelCompradores extends PanelBase {

    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"ID", "RUT", "Nombre", "Boletas", "Total comprado"}, 0) {
        @Override public boolean isCellEditable(int fila, int columna) { return false; }
    };

    public PanelCompradores(ServicioSupermercadoUI servicio) {
        super(servicio);
        add(encabezado("Compradores", "Mantén los datos de clientes y revisa su actividad."), BorderLayout.NORTH);
        JPanel tarjeta = tarjeta();
        tarjeta.setPreferredSize(new Dimension(850, 300));
        tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        JLabel titulo = new JLabel("Registro de clientes");
        titulo.setFont(Estilo.FUENTE_TARJETA);
        titulo.setForeground(Estilo.AZUL);
        JButton nuevo = Estilo.boton("＋ Nuevo comprador", Estilo.TURQUESA);
        nuevo.setPreferredSize(new Dimension(220, 48));
        nuevo.setMinimumSize(new Dimension(220, 48));
        JTable tabla = new JTable(modelo);
        Estilo.tabla(tabla);
        JScrollPane desplazamiento = new JScrollPane(tabla);
        desplazamiento.setPreferredSize(new Dimension(850, 220));

        JPanel pie = new JPanel(new BorderLayout(12, 0));
        pie.setOpaque(false);
        pie.setPreferredSize(new Dimension(850, 64));
        pie.add(nuevo, BorderLayout.EAST);

        tarjeta.add(titulo, BorderLayout.NORTH);
        tarjeta.add(desplazamiento, BorderLayout.CENTER);
        JPanel zona = new JPanel(new BorderLayout());
        zona.setOpaque(false);
        zona.add(tarjeta, BorderLayout.NORTH);
        add(zona, BorderLayout.CENTER);
        add(pie, BorderLayout.SOUTH);
        nuevo.addActionListener(e -> agregar());
    }

    public void actualizar() {
        modelo.setRowCount(0);
        for (Comprador comprador : servicio.compradores()) {
            modelo.addRow(new Object[]{comprador.getIdComprador(), comprador.getRutComprador(),
                comprador.getAlias(), comprador.getHistorialBoletas().size(),
                String.format("$%,.0f", comprador.obtenerCriterioRanking())});
        }
    }

    private void agregar() {
        JTextField rut = new JTextField(18);
        JTextField nombre = new JTextField(18);
        JPanel formulario = new JPanel(new java.awt.GridLayout(0, 2, 8, 8));
        formulario.add(new JLabel("RUT")); formulario.add(rut);
        formulario.add(new JLabel("Nombre / alias")); formulario.add(nombre);
        Object[] botones = {"Registrar", "Cancelar"};
        int resultado = JOptionPane.showOptionDialog(this, formulario, "Nuevo comprador",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, botones, botones[0]);
        if (resultado != 0) return;
        String rutIngresado = rut.getText().trim();
        String nombreIngresado = nombre.getText().trim();
        if (rutIngresado.isEmpty() || nombreIngresado.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el RUT y el nombre del comprador.",
                    "Faltan datos", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int confirmar = JOptionPane.showConfirmDialog(this,
                "¿Registrar a " + nombreIngresado + " con RUT " + rutIngresado + "?",
                "Confirmar comprador", JOptionPane.YES_NO_OPTION);
        if (confirmar != JOptionPane.YES_OPTION) return;
        try {
            servicio.agregarComprador(rutIngresado, nombreIngresado);
            actualizar();
            JOptionPane.showMessageDialog(this, "Comprador registrado:\n"
                    + nombreIngresado + " · RUT " + rutIngresado, "Registro completo",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (DatoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo agregar",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}